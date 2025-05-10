package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.Role
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
        val keycloakId = keycloakAdminClient.createUser(profileDto)

        assignRole(keycloakId, Role.USER)

        return keycloakId
    }

    fun updateUser(keycloakId: String, firstName: String, lastName: String, email: String) {
        keycloakAdminClient.updateUser(keycloakId, firstName, lastName, email)
    }

    fun assignRole(keycloakId: String, role: Role) {
        keycloakAdminClient.assignRoleToUser(keycloakId, role.name)
    }

    fun deleteUser(keycloakId: String) {
        keycloakAdminClient.deleteUser(keycloakId)
    }
}
