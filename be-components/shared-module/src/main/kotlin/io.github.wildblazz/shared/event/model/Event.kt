package io.github.wildblazz.shared.event.model

import java.io.Serializable
import java.util.*

abstract class Event : Serializable {
    val eventId: String = UUID.randomUUID().toString()
    abstract var eventType: String
    val publishedAt: Date = Date()
}
