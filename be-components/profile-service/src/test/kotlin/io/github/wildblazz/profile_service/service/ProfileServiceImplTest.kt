package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.configuration.TestConfiguration
import io.github.wildblazz.profile_service.model.Coordinates
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.repository.ProfileRepository
import io.github.wildblazz.shared.event.service.EventService
import io.github.wildblazz.shared.exception.types.NotFoundException
import io.github.wildblazz.shared.model.Gender
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDate

@SpringBootTest
@Import(TestConfiguration::class)
@ActiveProfiles("test")
class ProfileServiceImplTest {

    @MockitoBean
    private lateinit var profileRepository: ProfileRepository

    @MockitoBean
    private lateinit var keycloakService: KeycloakService

    @MockitoBean
    private lateinit var eventService: EventService

    @MockitoBean
    private lateinit var geocodingService: GeocodingService

    @Autowired
    private lateinit var profileService: ProfileServiceImpl


    fun validProfile(): Profile {
        return Profile(
            keycloakId = "valid-keycloak-id",
            email = "test@example.com",
            userName = "testuser",
            firstName = "Test",
            lastName = "User",
            coordinates = Coordinates(0.0, 0.0),
            city = "Test City",
            gender = Gender.MALE,
            bio = null,
            interests = emptyList(),
            birthDate = LocalDate.now(),
            targetGender = Gender.MALE,
            searchRadiusKm = 10
        )
    }

    @Test
    fun getProfileByKeycloakIdReturnsProfileDtoWhenProfileExists() {
        val keycloakId = "valid-keycloak-id"
        val profile = validProfile()
        `when`(profileRepository.findByKeycloakId(keycloakId)).thenReturn(profile)

        val result = profileService.getProfileByKeycloakId(keycloakId)

        assertNotNull(result)
        assertEquals(profile.keycloakId, result.keyCloakId)
        assertEquals(profile.userName, result.userName)
    }

    @Test
    fun getProfileByKeycloakIdThrowsNotFoundExceptionWhenProfileDoesNotExist() {
        val keycloakId = "non-existent-keycloak-id"
        `when`(profileRepository.findByKeycloakId(keycloakId)).thenReturn(null)

        assertThrows(NotFoundException::class.java) {
            profileService.getProfileByKeycloakId(keycloakId)
        }
    }
}
