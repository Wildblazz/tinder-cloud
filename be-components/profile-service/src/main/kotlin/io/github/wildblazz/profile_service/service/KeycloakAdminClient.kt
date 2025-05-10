package io.github.wildblazz.profile_service.service

import io.github.wildblazz.shared.exception.types.KeyCloakException
import io.github.wildblazz.shared.exception.types.NotFoundException
import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.model.KeycloakRole
import io.github.wildblazz.profile_service.model.KeycloakUser
import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Instant

@Component
class KeycloakAdminClient(
    private val restTemplate: RestTemplate,
    private val authorizedClientManager: OAuth2AuthorizedClientManager,
    private val clientRegistrationRepository: ClientRegistrationRepository,
    @Value("\${keycloak.admin-url}") private val keycloakAdminUrl: String,
) {
    private val token: String = ""
    private var tokenExpirationTime: Instant = Instant.now();

    private fun getAccessToken(): String {
        if (!isTokenValid()) {
            val clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak")
                ?: throw KeyCloakException(Constants.EXCEPTION_KEYCLOAK_CLIENT_REGISTRATION)

            val authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistration.registrationId)
                .principal("system")
                .build()

            val authorizedClient: OAuth2AuthorizedClient = authorizedClientManager.authorize(authorizeRequest)
                ?: throw IllegalStateException(Constants.EXCEPTION_KEYCLOAK_CLIENT_AUTHORIZATION)
            tokenExpirationTime = authorizedClient.accessToken.expiresAt!!
            return authorizedClient.accessToken.tokenValue
        } else return token
    }

    private fun isTokenValid(): Boolean {
        return token.isNotBlank() && tokenExpirationTime > Instant.now()
    }

    fun findUserByEmail(email: String): KeycloakUser? {
        val url = "${keycloakAdminUrl}/users?email=$email"
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${getAccessToken()}")
        }
        val response: ResponseEntity<Array<KeycloakUser>>;
        try {
            response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity<Any>(headers), Array<KeycloakUser>::class.java
            )
        } catch (_: RestClientException) {
            return null;
        }

        return response.body?.firstOrNull()
    }

    fun createUser(profileDto: CreateProfileDto): String {
        val url = "${keycloakAdminUrl}/users"
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${getAccessToken()}")
            set("Content-Type", "application/json")
        }
        val userPayload = mapOf(
            "username" to profileDto.userName,
            "firstName" to profileDto.firstName,
            "lastName" to profileDto.lastName,
            "email" to profileDto.email,
            "enabled" to true,
            "credentials" to listOf(
                mapOf(
                    "type" to "password",
                    "value" to profileDto.password,
                    "temporary" to false
                )
            )
        )
        val response = restTemplate.exchange(
            url, HttpMethod.POST, HttpEntity(userPayload, headers), Map::class.java
        )
        return response.headers["Location"]?.first()?.split("/")?.last()
            ?: throw KeyCloakException(Constants.EXCEPTION_KEYCLOAK_USER_CREATION)
    }

    fun assignRoleToUser(keycloakId: String, roleName: String) {
        val role = getRole(roleName);
        if (role != null) {
            val url = "${keycloakAdminUrl}/users/$keycloakId/role-mappings/realm"
            val headers = HttpHeaders().apply {
                set("Authorization", "Bearer ${getAccessToken()}")
                set("Content-Type", "application/json")
            }
            val rolePayload = listOf(
                mapOf(
                    "id" to role.id,
                    "name" to role.name,
                    "clientRole" to false
                )
            )
            restTemplate.exchange(url, HttpMethod.POST, HttpEntity(rolePayload, headers), Void::class.java)
        }
    }

    fun getRole(roleName: String): KeycloakRole? {
        val url = "${keycloakAdminUrl}/roles/$roleName"
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${getAccessToken()}")
            set("Content-Type", "application/json")
        }
        return try {
            restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity<Any>(headers), KeycloakRole::class.java
            ).body
        } catch (_: RestClientException) {
            throw NotFoundException(Constants.EXCEPTION_KEYCLOAK_ROLE_NOT_FOUND, arrayOf(roleName))
        }
    }

    fun updateUser(keycloakId: String, firstName: String, lastName: String, email: String) {
        val url = "${keycloakAdminUrl}/users/$keycloakId"
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${getAccessToken()}")
            set("Content-Type", "application/json")
        }
        val userPayload = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )
        restTemplate.exchange(
            url, HttpMethod.PUT, HttpEntity(userPayload, headers), Void::class.java
        )
    }

    fun deleteUser(keycloakId: String) {
        val url = "${keycloakAdminUrl}/users/$keycloakId"
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${getAccessToken()}")
        }
        restTemplate.exchange(
            url, HttpMethod.DELETE, HttpEntity<Any>(headers), Void::class.java
        )
    }
}
