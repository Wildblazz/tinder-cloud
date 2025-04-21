package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import org.springframework.stereotype.Service

@Service
class KeycloakService(
    private val keycloakAdminClient: KeycloakAdminClient
) {

    fun getOrCreateUser(profileDto: CreateProfileDto): String {
        val existingUser = keycloakAdminClient.findUserByEmail(profileDto.email)
        if (existingUser != null) {
            return existingUser.id
        }
        val newUserId = keycloakAdminClient.createUser(profileDto)

        keycloakAdminClient.assignRoleToUser(newUserId, "USER")

        return newUserId
    }

    fun updateUser(userId: String, firstName: String, lastName: String, email: String) {
        keycloakAdminClient.updateUser(userId, firstName, lastName, email)
    }

    fun deleteUser(userId: String) {
        keycloakAdminClient.deleteUser(userId)
    }
}
