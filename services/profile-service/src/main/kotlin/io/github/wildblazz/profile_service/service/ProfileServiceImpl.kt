package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.exception.ProfileNotFoundException
import io.github.wildblazz.profile_service.exception.UnauthorizedAccessException
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.ProfileDto
import io.github.wildblazz.profile_service.repository.ProfileRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository
) : ProfileService {

    override fun getProfileById(id: UUID): ProfileDto {
        val profile =
            profileRepository.findByIdOrNull(id) ?: throw ProfileNotFoundException("Profile with id $id not found")
        return mapToDto(profile)
    }

    override fun getProfileByUserId(userId: String): ProfileDto {
        val profile = profileRepository.findByUserId(userId)
            ?: throw ProfileNotFoundException("Profile for user $userId not found")
        return mapToDto(profile)
    }

    @Transactional
    override fun createProfile(profileDto: ProfileDto, userId: String): ProfileDto {
        // Check if profile already exists for this user
        if (profileRepository.existsByUserId(userId)) {
            throw IllegalStateException("Profile already exists for user $userId")
        }

        val profile = Profile(
            id = UUID.randomUUID(),
            userId = userId,
            name = profileDto.name,
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
    override fun updateProfile(id: UUID, profileDto: ProfileDto, userId: String): ProfileDto {
        val existingProfile =
            profileRepository.findByIdOrNull(id) ?: throw ProfileNotFoundException("Profile with id $id not found")

        if (existingProfile.userId != userId) {
            throw UnauthorizedAccessException("You don't have permission to update this profile")
        }

        existingProfile.apply {
            name = profileDto.name
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
    override fun deleteProfile(id: UUID, userId: String) {
        val profile =
            profileRepository.findByIdOrNull(id) ?: throw ProfileNotFoundException("Profile with id $id not found")

        if (profile.userId != userId) {
            throw UnauthorizedAccessException("You don't have permission to delete this profile")
        }

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
        return ProfileDto(id = profile.id,
            name = profile.name,
            age = profile.age,
            gender = profile.gender,
            bio = profile.bio,
            location = profile.location,
            interests = profile.interests,
            photos = profile.photos.map { it.url })
    }
}
