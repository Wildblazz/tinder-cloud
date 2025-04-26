package io.github.wildblazz.profile_service.service

import io.github.wildblazz.common.exception.DuplicateException
import io.github.wildblazz.common.exception.NotFoundException
import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.common.Constants.EXCEPTION_PROFILE_NOT_FOUND
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import io.github.wildblazz.profile_service.model.dto.ProfileDto
import io.github.wildblazz.profile_service.model.dto.SearchCriteria
import io.github.wildblazz.profile_service.model.dto.UpdateProfileDto
import io.github.wildblazz.profile_service.repository.ProfileRepository
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val keycloakService: KeycloakService
) : ProfileService {

    override fun getProfileByUserId(userId: String): ProfileDto {
        val profile = getUserProfile(userId)
        return mapToDto(profile)
    }

    @Transactional
    override fun createProfile(profileDto: CreateProfileDto): ProfileDto {
        if (profileRepository.existsByEmail(profileDto.email)) {
            throw DuplicateException(Constants.EXCEPTION_PROFILE_DUPLICATE, arrayOf(profileDto.email))
        }

        val keycloakUserId = keycloakService.getOrCreateUser(profileDto)

        val profile = Profile(
            userId = keycloakUserId,
            email = profileDto.email,
            userName = profileDto.userName,
            firstName = profileDto.firstName,
            lastName = profileDto.lastName,
            age = profileDto.age,
            gender = profileDto.gender,
            bio = profileDto.bio,
            location = profileDto.location,
            interests = profileDto.interests
        )

        val savedProfile = profileRepository.save(profile)
        return mapToDto(savedProfile)
    }

    @Transactional
    override fun updateProfile(userId: String, profileDto: UpdateProfileDto): ProfileDto {
        val existingProfile = getUserProfile(userId)

        existingProfile.apply {
            firstName = profileDto.firstName?.takeIf { it.isNotBlank() } ?: firstName
            lastName = profileDto.lastName?.takeIf { it.isNotBlank() } ?: lastName
            email = profileDto.email?.takeIf { it.isNotBlank() } ?: email
            bio = profileDto.bio?.takeIf { it.isNotBlank() } ?: bio
            location = profileDto.location?.takeIf { it.isNotBlank() } ?: location
            interests = profileDto.interests?.takeIf { it.isNotEmpty() } ?: interests
        }

        keycloakService.updateUser(userId, existingProfile.firstName, existingProfile.lastName, existingProfile.email)

        val updatedProfile = profileRepository.save(existingProfile)
        return mapToDto(updatedProfile)
    }

    @Transactional
    override fun deleteProfile(userId: String) {
        val profile = getUserProfile(userId)

        keycloakService.deleteUser(userId)
        profileRepository.delete(profile)
    }

    override fun searchProfiles(criteria: SearchCriteria, page: Int, size: Int): Page<ProfileDto> {
        val pageable = PageRequest.of(page, size)

        val profiles = profileRepository.findAll({ root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.age?.let { predicates.add(cb.equal(root.get<Int>("age"), it)) }
            criteria.gender?.let { predicates.add(cb.equal(root.get<String>("gender"), it)) }
            criteria.location?.let { predicates.add(cb.like(cb.lower(root.get("location")), "%${it.lowercase()}%")) }
            criteria.bio?.let { predicates.add(cb.like(cb.lower(root.get("bio")), "%${it.lowercase()}%")) }
            criteria.interests?.let { interests ->
                val interestPredicates = interests.map { interest ->
                    cb.isMember(interest.lowercase(), root.get<Set<String>>("interests"))
                }
                predicates.add(cb.or(*interestPredicates.toTypedArray()))
            }
            cb.and(*predicates.toTypedArray())
        }, pageable)

        return profiles.map { mapToDto(it) }
    }

    private fun getUserProfile(userId: String): Profile {
        return profileRepository.findByUserId(userId)
            ?: throw NotFoundException(EXCEPTION_PROFILE_NOT_FOUND, arrayOf(userId))
    }

    private fun mapToDto(profile: Profile): ProfileDto {
        return ProfileDto(
            userId = profile.userId,
            userName = profile.userName,
            firstName = profile.firstName,
            lastName = profile.lastName,
            email = profile.email,
            age = profile.age,
            gender = profile.gender,
            bio = profile.bio,
            location = profile.location,
            interests = profile.interests,
            photos = profile.photos?.map { it.url } ?: emptyList()
        )
    }
}
