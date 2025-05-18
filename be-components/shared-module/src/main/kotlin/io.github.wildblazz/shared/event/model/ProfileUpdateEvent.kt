package io.github.wildblazz.shared.event.model

class ProfileUpdateEvent(
    val keycloakId: String,
    val firstName: String,
    val lastName: String,
    val searchRadiusKm: Int,
    val latitude: Double?,
    val longitude: Double?,
    val city: String,
    val interests: List<String>,
    override var eventType: String = "PROFILE_CREATE"
) : Event() {}
