package io.github.wildblazz.shared.event.service

import io.github.wildblazz.shared.event.model.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class EventService(private val kafkaTemplate: KafkaTemplate<String, Any>) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun publish(event: Event) {
        val topic = resolveTopicName(event)
        logger.info("Publishing event: ${event.toString()}")
        logger.info("Topic: $topic")
        kafkaTemplate.send(topic, event.eventId, event)
    }

    fun resolveTopicName(event: Event): String {
        return event::class.simpleName!!
            .replace(Regex("([a-z])([A-Z])"), "$1-$2")
            .lowercase()
    }
}
