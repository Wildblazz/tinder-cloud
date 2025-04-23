package io.github.wildblazz.profile_service.model

data class KeycloakUser(
    val id: String,
    val username: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
)
