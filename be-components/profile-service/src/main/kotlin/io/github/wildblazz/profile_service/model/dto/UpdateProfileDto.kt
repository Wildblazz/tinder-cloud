package io.github.wildblazz.profile_service.model.dto

import io.github.wildblazz.profile_service.model.Coordinates
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class UpdateProfileDto(
    @field:Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
    val firstName: String?,

    @field:Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
    val lastName: String?,

    @field:Email(message = "Email must be valid")
    val email: String?,

    @field:Min(value = 1, message = "Search radius must be at least 1 km")
    @field:Max(value = 100, message = "Search radius must not exceed 100 km")
    val searchRadiusKm: Int? = 30,

    @field:Valid
    val coordinates: Coordinates? = null,

    @field:Size(max = 128, message = "City must not exceed 128 characters")
    val city: String? = null,

    @field:Size(max = 256, message = "Bio must not exceed 256 characters")
    val bio: String? = null,

    @field:Valid
    val interests: List<@Size(max = 32, message = "Each interest must not exceed 32 characters") String>? = emptyList()
)
