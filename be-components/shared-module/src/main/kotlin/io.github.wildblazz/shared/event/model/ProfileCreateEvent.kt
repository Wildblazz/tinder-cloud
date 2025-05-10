package io.github.wildblazz.shared.event.model

class ProfileCreateEvent(
    val keycloakId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val location: String?,
    override var eventType: String = "PROFILE_CREATE"
) : Event() {}
