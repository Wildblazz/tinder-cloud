package io.github.wildblazz.profile_service.model.dto

import java.util.*

data class ProfileDto(
    val id: UUID? = null,
    val name: String,
    val age: Int,
    val gender: String,
    val bio: String? = null,
    val location: String? = null,
    val interests: List<String> = emptyList(),
    val photos: List<String> = emptyList()
)
