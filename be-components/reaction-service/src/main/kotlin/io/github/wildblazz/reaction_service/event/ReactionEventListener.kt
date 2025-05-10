package io.github.wildblazz.reaction_service.event

import io.github.wildblazz.reaction_service.service.ReactionService
import io.github.wildblazz.shared.event.model.ProfileCreateEvent
import io.github.wildblazz.shared.event.model.ProfileDeleteEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ReactionEventListener(private val reactionService: ReactionService) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["profile-create-event"], groupId = "reaction-service")
    fun handleProfileCreateEvent(event: ProfileCreateEvent) {
        logger.info("Received ProfileCreateEvent with user_id: " + event.keycloakId)
//        TODO implement
    }

    @KafkaListener(topics = ["profile-delete-event"], groupId = "reaction-service")
    fun handleDeleteProfileEvent(event: ProfileDeleteEvent) {
        logger.info("Received ProfileDeleteEvent with user_id: " + event.keycloakId)
        reactionService.deleteReactionsByUserId(event.keycloakId)
    }
}
