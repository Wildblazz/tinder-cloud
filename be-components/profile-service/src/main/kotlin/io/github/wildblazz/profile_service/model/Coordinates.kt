package io.github.wildblazz.profile_service.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Embeddable
data class Coordinates(
    @field:Min(-90) @field:Max(90)
    @Column(name = "latitude")
    val latitude: Double,

    @field:Min(-180) @field:Max(180)
    @Column(name = "longitude")
    val longitude: Double
)
