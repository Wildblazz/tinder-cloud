package io.github.wildblazz.match_service.event

import io.github.wildblazz.match_service.service.MatchService
import io.github.wildblazz.shared.event.model.LikeCreateEvent
import io.github.wildblazz.shared.event.model.LikeDeleteEvent
import io.github.wildblazz.shared.event.model.ProfileDeleteEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EventListener(private val matchService: MatchService) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["like-create-event"], groupId = "match-service")
    fun handleLikeCreateEvent(event: LikeCreateEvent) {
        logger.info("Received LikeCreateEvent with user_id_1: " + event.userId1 + "and user_id_2: " + event.userId1)
        matchService.createMatch(event.userId1, event.userId2)
    }

    @KafkaListener(topics = ["like-delete-event"], groupId = "match-service")
    fun handleLikeCreateEvent(event: LikeDeleteEvent) {
        logger.info("Received LikeDeleteEvent with user_id_1: " + event.userId1 + "and user_id_2: " + event.userId1)
        matchService.deleteMatch(event.userId1, event.userId2)
    }

    @KafkaListener(topics = ["profile-delete-event"], groupId = "match-service")
    fun handleDeleteProfileEvent(event: ProfileDeleteEvent) {
        logger.info("Received ProfileDeleteEvent with user_id: " + event.keycloakId)
        matchService.deleteMatchesByUserId(event.keycloakId)
    }
}
