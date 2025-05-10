package io.github.wildblazz.profile_service.model


import jakarta.persistence.*
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
    var userName: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false)
    var gender: String,

    @Column(columnDefinition = "TEXT")
    var bio: String? = null,

//    TODO add PostGIS support
    var location: String? = null,

    @ElementCollection
    @CollectionTable(name = "profile_interests", joinColumns = [JoinColumn(name = "profile_id")])
    @Column(name = "interest")
    var interests: List<String> = listOf(),

    @OneToMany(mappedBy = "profile", cascade = [CascadeType.ALL], orphanRemoval = true)
    var photos: MutableList<Photo> = mutableListOf(),

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
