package io.github.wildblazz.recommendation_service.event

import io.github.wildblazz.recommendation_service.entity.RecommendationProfile
import io.github.wildblazz.recommendation_service.service.RecommendationService
import io.github.wildblazz.shared.event.model.ProfileCreateEvent
import io.github.wildblazz.shared.event.model.ProfileDeleteEvent
import io.github.wildblazz.shared.event.model.ProfileUpdateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RecommendationEventListener(private val recommendationService: RecommendationService) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["profile-create-event"], groupId = "recommendation-service")
    fun handleProfileCreateEvent(event: ProfileCreateEvent) {
        logger.info("Received ProfileCreateEvent with user_id: ${event.keycloakId}")

        recommendationService.saveNewProfile(
            with(event) {
                RecommendationProfile(
                    keycloakId = keycloakId,
                    userName = userName,
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    latitude = latitude,
                    longitude = longitude,
                    searchRadiusKm = searchRadiusKm ?: 30,
                    gender = gender,
                    interests = interests,
                    lastUpdatedAt = Instant.now()
                )
            }
        )
    }

    @KafkaListener(topics = ["profile-delete-event"], groupId = "recommendation-service")
    fun handleDeleteProfileEvent(event: ProfileDeleteEvent) {
        logger.info("Received ProfileDeleteEvent with user_id: " + event.keycloakId)

        recommendationService.removeProfile(event.keycloakId)
    }

    @KafkaListener(topics = ["profile-update-event"], groupId = "recommendation-service")
    fun handleDeleteProfileEvent(event: ProfileUpdateEvent) {
        logger.info("Received ProfileUpdateEvent with user_id: " + event.keycloakId)

        recommendationService.updateProfile(event)
    }
}
