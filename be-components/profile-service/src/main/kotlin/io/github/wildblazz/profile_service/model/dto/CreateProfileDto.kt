package io.github.wildblazz.profile_service.model.dto

import io.github.wildblazz.profile_service.model.Coordinates
import io.github.wildblazz.profile_service.model.Gender
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDate

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

    @field:NotNull(message = "Birth date must not be null")
    val birthDate: LocalDate,

    @field:NotNull(message = "Gender must not be null")
    val gender: Gender,

    @field:NotNull(message = "Target gender must not be null")
    val targetGender: Gender,

    @field:Min(value = 1, message = "Search radius must be at least 1 km")
    @field:Max(value = 100, message = "Search radius must not exceed 100 km")
    val searchRadiusKm: Int = 30,

    @field:Valid
    val coordinates: Coordinates? = null,

    @field:NotNull(message = "City must not be null")
    @field:Size(max = 128, message = "City must not exceed 128 characters")
    val city: String,

    @field:Size(max = 256, message = "Bio must not exceed 256 characters")
    val bio: String? = null,

    @field:Valid
    val interests: List<@Size(max = 32, message = "Each interest must not exceed 32 characters") String> = emptyList()
)
