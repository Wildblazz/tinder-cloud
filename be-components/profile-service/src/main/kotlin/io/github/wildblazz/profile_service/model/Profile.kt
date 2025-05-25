package io.github.wildblazz.profile_service.model

import io.github.wildblazz.shared.model.Gender
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "profiles",
    indexes = [Index(name = "idx_keycloak_id", columnList = "keycloakId")]
)
data class Profile(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var keycloakId: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    var userName: String,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(name = "birth_date")
    val birthDate: LocalDate,

    @Column(nullable = false)
    val gender: Gender,

    @Column(nullable = false)
    var targetGender: Gender,

    @Column(name = "search_radius_km")
    var searchRadiusKm: Int = 30,

    @Embedded
    var coordinates: Coordinates,

    @Column(nullable = false)
    var city: String,

    @Column(columnDefinition = "TEXT")
    var bio: String?,

    @ElementCollection
    @CollectionTable(name = "profile_interests", joinColumns = [JoinColumn(name = "profile_id")])
    @Column(name = "interest")
    var interests: List<String>,

    @OneToMany(mappedBy = "profile", cascade = [CascadeType.ALL], orphanRemoval = true)
    val photos: MutableList<Photo> = mutableListOf(),

    @Column(name = "last_active_at", nullable = false)
    var lastActiveAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @PreUpdate
    fun onUpdate() {
        lastActiveAt = LocalDateTime.now()
    }
}
