package io.github.wildblazz.profile_service.repository


import io.github.wildblazz.profile_service.model.Profile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : JpaRepository<Profile, UUID> {
    fun findByUserId(userId: String): Profile?
    fun existsByEmail(email: String): Boolean

    fun findByAge(age: Int, pageable: Pageable): Page<Profile>
    fun findByGender(gender: String, pageable: Pageable): Page<Profile>
    fun findByLocationContainingIgnoreCase(location: String, pageable: Pageable): Page<Profile>

    fun findByAgeAndGender(age: Int, gender: String, pageable: Pageable): Page<Profile>
    fun findByAgeAndLocationContainingIgnoreCase(age: Int, location: String, pageable: Pageable): Page<Profile>
    fun findByGenderAndLocationContainingIgnoreCase(gender: String, location: String, pageable: Pageable): Page<Profile>

    fun findByAgeAndGenderAndLocationContainingIgnoreCase(
        age: Int,
        gender: String,
        location: String,
        pageable: Pageable
    ): Page<Profile>
}
