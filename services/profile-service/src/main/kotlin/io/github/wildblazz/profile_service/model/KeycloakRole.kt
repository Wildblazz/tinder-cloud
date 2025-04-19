package io.github.wildblazz.profile_service.model

data class KeycloakRole(
    val id: String,
    val name: String,
    val description: String? = null,
    val composite: Boolean = false,
    val clientRole: Boolean = false,
    val containerId: String
)
