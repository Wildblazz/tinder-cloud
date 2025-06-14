package io.github.wildblazz.shared.event.model

class LikeDeleteEvent(
    val userId1: String,
    val userId2: String,
    override var eventType: String = "LIKE_DELETE_EVENT"
) : Event() {}
