package io.github.wildblazz.shared.event.config

import io.github.classgraph.ClassGraph
import io.github.wildblazz.shared.event.model.Event
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration
class TopicConfiguration(
    private val kafkaProperties: KafkaProperties,
) {
    private val eventClasses: List<KClass<out Event>> = findEventClasses()

    @Bean
    fun topics(): List<NewTopic> {
        return eventClasses.map { clazz ->
            val topicName = clazz.simpleName!!.uppercase()
            val partitions = kafkaProperties.partitions[topicName] ?: kafkaProperties.defaultPartitions
            NewTopic(topicName, partitions, 1)
        }
    }

    private fun findEventClasses(): List<KClass<out Event>> {
        return ClassGraph()
            .acceptPackages("io.github.wildblazz.shared.event.model")
            .scan()
            .getSubclasses(Event::class.qualifiedName)
            .loadClasses(Event::class.java)
            .map { it.kotlin as KClass<out Event> }
    }
}
