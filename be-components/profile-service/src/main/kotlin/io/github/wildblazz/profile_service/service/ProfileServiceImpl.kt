package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.common.Constants.EXCEPTION_PROFILE_NOT_FOUND
import io.github.wildblazz.profile_service.model.Coordinates
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.*
import io.github.wildblazz.profile_service.repository.ProfileRepository
import io.github.wildblazz.shared.event.model.ProfileCreateEvent
import io.github.wildblazz.shared.event.model.ProfileDeleteEvent
import io.github.wildblazz.shared.event.model.ProfileUpdateEvent
import io.github.wildblazz.shared.event.service.EventService
import io.github.wildblazz.shared.exception.types.DuplicateException
import io.github.wildblazz.shared.exception.types.NotFoundException
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Period

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val keycloakService: KeycloakService,
    private val eventService: EventService,
    private val geocodingService: GeocodingService
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

        val profile = profileDto.run {
            Profile(
                keycloakId = keycloakId,
                email = email,
                userName = userName,
                firstName = firstName,
                lastName = lastName,
                coordinates = resolveCoordinates(coordinates, city),
                city = city,
                gender = gender,
                bio = bio,
                interests = interests,
                birthDate = birthDate,
                targetGender = targetGender,
                searchRadiusKm = searchRadiusKm,
            )
        }

        val savedProfile = profileRepository.save(profile)

        eventService.publish(
            profileDto.run {
                ProfileCreateEvent(
                    keycloakId = profile.keycloakId,
                    userName = profile.userName,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    gender = profile.gender,
                    city = profile.city,
                    latitude = profile.coordinates.latitude,
                    longitude = profile.coordinates.longitude,
                    searchRadiusKm = profile.searchRadiusKm,
                    interests = profile.interests,
                )
            }
        )

        return mapToDto(savedProfile)
    }

    @Transactional
    override fun updateProfile(keycloakId: String, profileDto: UpdateProfileDto): ProfileDto {
        val existingProfile = getUserProfile(keycloakId)
        profileDto.email?.let { isProfileExistsByEmail(it) }
        var cityChanged = false

        existingProfile.apply {
            firstName = profileDto.firstName?.takeIf { it.isNotBlank() } ?: firstName
            lastName = profileDto.lastName?.takeIf { it.isNotBlank() } ?: lastName
            email = profileDto.email?.takeIf { it.isNotBlank() } ?: email
            searchRadiusKm = profileDto.searchRadiusKm.takeIf { true } ?: searchRadiusKm
            coordinates = profileDto.coordinates?.takeIf { true } ?: coordinates
            city = profileDto.city?.takeIf { it.isNotBlank() }?.let { newCity ->
                if (newCity != city) {
                    cityChanged = true
                }
                newCity
            } ?: city
            bio = profileDto.bio?.takeIf { it.isNotBlank() } ?: bio
            interests = profileDto.interests?.takeIf { it.isNotEmpty() } ?: interests
        }
        if (cityChanged && profileDto.coordinates == null) {
            existingProfile.coordinates = geocodingService.resolveCityCoordinates(existingProfile.city)
        }

        keycloakService.updateUser(
            keycloakId,
            existingProfile.firstName,
            existingProfile.lastName,
            existingProfile.email
        )

        val updatedProfile = profileRepository.save(existingProfile)

        eventService.publish(
            existingProfile.run {
                ProfileUpdateEvent(
                    keycloakId = keycloakId,
                    firstName = firstName,
                    lastName = lastName,
                    searchRadiusKm = searchRadiusKm,
                    interests = interests,
                    city = city,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude,
                )
            }
        )
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

    override fun isProfileExistsByEmail(email: String) {
        if (profileRepository.existsByEmail(email)) {
            throw DuplicateException(Constants.EXCEPTION_PROFILE_DUPLICATE, arrayOf(email))
        }
    }

    private fun getUserProfile(keycloakId: String): Profile {
        return profileRepository.findByKeycloakId(keycloakId)
            ?: throw NotFoundException(EXCEPTION_PROFILE_NOT_FOUND, arrayOf(keycloakId))
    }

    private fun resolveCoordinates(coordinates: Coordinates?, city: String): Coordinates =
        coordinates ?: geocodingService.resolveCityCoordinates(city)


    private fun mapToDto(profile: Profile): ProfileDto {
        return ProfileDto(
            keyCloakId = profile.keycloakId,
            userName = profile.userName,
            firstName = profile.firstName,
            lastName = profile.lastName,
            email = profile.email,
            birthDate = profile.birthDate,
            age = Period.between(profile.birthDate, LocalDate.now()).years,
            gender = profile.gender,
            bio = profile.bio,
            city = profile.city,
            interests = profile.interests,
            photos = profile.photos.map { it.url }
        )
    }
}
