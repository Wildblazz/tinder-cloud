package io.github.wildblazz.shared.event.model

import io.github.wildblazz.shared.model.Gender

class ProfileCreateEvent(
    val keycloakId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val searchRadiusKm: Int?,
    var interests: List<String>,
    override var eventType: String = "PROFILE_CREATE"
) : Event() {}
