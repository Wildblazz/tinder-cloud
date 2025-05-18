package io.github.wildblazz.profile_service.model.dto

import io.github.wildblazz.profile_service.model.Gender
import java.time.LocalDate

data class ProfileDto(
    val keyCloakId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    val age: Int,
    val gender: Gender,
    val bio: String? = null,
    val interests: List<String> = emptyList(),
    val photos: List<String> = emptyList(),
    val city: String
)
