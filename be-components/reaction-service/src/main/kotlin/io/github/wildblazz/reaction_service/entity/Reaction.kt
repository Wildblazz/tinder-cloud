package io.github.wildblazz.reaction_service.entity

import io.github.wildblazz.reaction_service.model.dto.ReactionType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "reactions",
    indexes = [Index(name = "idx_user_id", columnList = "userId")]
)
data class Reaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val targetUserId: String,

    @Column(nullable = false)
    val type: ReactionType,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val isSuper: Boolean = false
)
