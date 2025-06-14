package io.github.wildblazz.shared.event.model

class LikeCreateEvent(
    val userId1: String,
    val userId2: String,
    override var eventType: String = "LIKE_CREATE_EVENT"
) : Event() {}
