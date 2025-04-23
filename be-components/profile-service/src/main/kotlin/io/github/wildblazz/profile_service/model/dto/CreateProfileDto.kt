package io.github.wildblazz.profile_service.model.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class CreateProfileDto(
    @field:Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
    val userName: String,

    @field:Size(min = 3, max = 128, message = "Password must be between 3 and 32 characters")
    val password: String,

    @field:Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
    val firstName: String,

    @field:Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
    val lastName: String,

    @field:Email(message = "Email must be valid")
    val email: String,

    @field:Max(value = 120, message = "Age must not exceed 120")
    @field:Min(value = 0, message = "Age must be at least 0")
    val age: Int,

    @field:Size(min = 1, max = 16, message = "Gender must be between 1 and 16 characters")
    val gender: String,

    @field:Size(max = 256, message = "Bio must not exceed 256 characters")
    val bio: String? = null,

    @field:Size(max = 128, message = "Location must not exceed 128 characters")
    val location: String? = null,

    @field:Valid
    val interests: List<@Size(max = 32, message = "Each interest must not exceed 32 characters") String> = emptyList(),

    @field:Valid
    val photos: List<@Size(max = 256, message = "Each photo URL must not exceed 256 characters") String> = emptyList()
)
