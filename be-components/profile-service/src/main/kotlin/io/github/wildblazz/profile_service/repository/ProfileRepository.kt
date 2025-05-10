package io.github.wildblazz.profile_service.repository


import io.github.wildblazz.profile_service.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface ProfileRepository : JpaRepository<Profile, UUID>, JpaSpecificationExecutor<Profile> {
    fun findByKeycloakId(keycloakId: String): Profile?
    fun existsByEmail(email: String): Boolean
}
