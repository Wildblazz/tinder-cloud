package io.github.wildblazz.profile_service.model.dto

data class ProfileDto(
    val keyCloakId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int,
    val gender: String,
    val bio: String? = null,
    val location: String? = null,
    val interests: List<String> = emptyList(),
    val photos: List<String> = emptyList()
)
