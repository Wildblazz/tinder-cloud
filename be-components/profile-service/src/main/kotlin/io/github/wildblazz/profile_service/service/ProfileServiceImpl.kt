package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.common.Constants.EXCEPTION_PROFILE_NOT_FOUND
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.*
import io.github.wildblazz.profile_service.repository.ProfileRepository
import io.github.wildblazz.shared.event.model.ProfileCreateEvent
import io.github.wildblazz.shared.event.model.ProfileDeleteEvent
import io.github.wildblazz.shared.event.service.EventService
import io.github.wildblazz.shared.exception.types.DuplicateException
import io.github.wildblazz.shared.exception.types.NotFoundException
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val keycloakService: KeycloakService,
    private val eventService: EventService
) : ProfileService {

    override fun getProfileByKeycloakId(keycloakId: String): ProfileDto {
        val profile = getUserProfile(keycloakId)
        return mapToDto(profile)
    }

    @Transactional
    override fun createProfile(profileDto: CreateProfileDto): ProfileDto {
        if (profileRepository.existsByEmail(profileDto.email)) {
            throw DuplicateException(Constants.EXCEPTION_PROFILE_DUPLICATE, arrayOf(profileDto.email))
        }

        val keycloakId = keycloakService.getOrCreateUser(profileDto)

        val profile = Profile(
            keycloakId = keycloakId,
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

        eventService.publish(ProfileCreateEvent(
            keycloakId = profile.keycloakId,
            userName = profileDto.userName,
            firstName = profileDto.firstName,
            lastName = profileDto.lastName,
            location = profileDto.location,
        ))

        return mapToDto(savedProfile)
    }

    @Transactional
    override fun updateProfile(keycloakId: String, profileDto: UpdateProfileDto): ProfileDto {
        val existingProfile = getUserProfile(keycloakId)

        existingProfile.apply {
            firstName = profileDto.firstName?.takeIf { it.isNotBlank() } ?: firstName
            lastName = profileDto.lastName?.takeIf { it.isNotBlank() } ?: lastName
            email = profileDto.email?.takeIf { it.isNotBlank() } ?: email
            bio = profileDto.bio?.takeIf { it.isNotBlank() } ?: bio
            location = profileDto.location?.takeIf { it.isNotBlank() } ?: location
            interests = profileDto.interests?.takeIf { it.isNotEmpty() } ?: interests
        }

        keycloakService.updateUser(
            keycloakId,
            existingProfile.firstName,
            existingProfile.lastName,
            existingProfile.email
        )

        val updatedProfile = profileRepository.save(existingProfile)
        return mapToDto(updatedProfile)
    }

    @Transactional
    override fun deleteProfile(keycloakId: String) {
        val profile = getUserProfile(keycloakId)

        keycloakService.deleteUser(keycloakId)
        profileRepository.delete(profile)

        eventService.publish(ProfileDeleteEvent(keycloakId = profile.keycloakId))
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

    override fun updateUserRole(userId: String, roleDto: UpdateRoleDto) {
        val user = getUserProfile(userId)
        keycloakService.assignRole(user.keycloakId, roleDto.role)
    }

    private fun getUserProfile(keycloakId: String): Profile {
        return profileRepository.findByKeycloakId(keycloakId)
            ?: throw NotFoundException(EXCEPTION_PROFILE_NOT_FOUND, arrayOf(keycloakId))
    }

    private fun mapToDto(profile: Profile): ProfileDto {
        return ProfileDto(
            keyCloakId = profile.keycloakId,
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
