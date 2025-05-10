package io.github.wildblazz.shared.event.model

class ProfileDeleteEvent(
    val keycloakId: String,
    override var eventType: String = "PROFILE_DELETE"
) : Event() {}
