package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.exception.NotFoundException
import io.github.wildblazz.profile_service.exception.UnauthorizedException
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import io.github.wildblazz.profile_service.model.dto.ProfileDto
import io.github.wildblazz.profile_service.repository.ProfileRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val keycloakService: KeycloakService
) : ProfileService {

    override fun getProfileByUserId(userId: String): ProfileDto {
        val profile = profileRepository.findByUserId(userId)
            ?: throw NotFoundException("Profile for user $userId not found")
        return mapToDto(profile)
    }

    @Transactional
    override fun createProfile(profileDto: CreateProfileDto): ProfileDto {
        if (profileRepository.existsByEmail(profileDto.email)) {
            throw IllegalStateException("Profile already exists for user ${profileDto.email}")
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
    override fun updateProfile(userId: String, profileDto: ProfileDto): ProfileDto {
        val existingProfile =
            profileRepository.findByUserId(userId)
                ?: throw NotFoundException("Profile with id $userId not found")

        if (profileDto.userId != userId) {
            throw UnauthorizedException("You don't have permission to update this profile")
        }

        existingProfile.apply {
            userName = profileDto.userName
            firstName = profileDto.firstName
            lastName = profileDto.lastName
            email = profileDto.email
            age = profileDto.age
            gender = profileDto.gender
            bio = profileDto.bio
            location = profileDto.location
            interests = profileDto.interests
        }

        val updatedProfile = profileRepository.save(existingProfile)
        return mapToDto(updatedProfile)
    }

    @Transactional
    override fun deleteProfile(userId: String) {
        val profile =
            profileRepository.findByUserId(userId)
                ?: throw NotFoundException("Profile with id $userId not found")

        profileRepository.delete(profile)
    }

    override fun searchProfiles(age: Int?, gender: String?, location: String?, page: Int, size: Int): List<ProfileDto> {
        val pageable = PageRequest.of(page, size)
        val profiles = when {
            age != null && gender != null && location != null -> profileRepository.findByAgeAndGenderAndLocationContainingIgnoreCase(
                age,
                gender,
                location,
                pageable
            )

            age != null && gender != null -> profileRepository.findByAgeAndGender(age, gender, pageable)

            age != null && location != null -> profileRepository.findByAgeAndLocationContainingIgnoreCase(
                age,
                location,
                pageable
            )

            gender != null && location != null -> profileRepository.findByGenderAndLocationContainingIgnoreCase(
                gender,
                location,
                pageable
            )

            age != null -> profileRepository.findByAge(age, pageable)

            gender != null -> profileRepository.findByGender(gender, pageable)

            location != null -> profileRepository.findByLocationContainingIgnoreCase(location, pageable)

            else -> profileRepository.findAll(pageable)
        }.content

        return profiles.map { mapToDto(it) }
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
