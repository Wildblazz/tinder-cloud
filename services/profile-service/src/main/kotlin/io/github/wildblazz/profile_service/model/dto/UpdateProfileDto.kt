package io.github.wildblazz.profile_service.model.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UpdateProfileDto(
    @field:Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
    val firstName: String? = null,

    @field:Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
    val lastName: String? = null,

    @field:Email(message = "Email must be valid")
    val email: String? = null,

    @field:Size(max = 256, message = "Bio must not exceed 256 characters")
    val bio: String? = null,

    @field:Size(max = 128, message = "Location must not exceed 128 characters")
    val location: String? = null,

    @field:Valid
    val interests: List<@Size(max = 32, message = "Each interest must not exceed 32 characters") String>? = null,
)
