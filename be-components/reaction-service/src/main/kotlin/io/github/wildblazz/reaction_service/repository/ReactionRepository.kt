package io.github.wildblazz.reaction_service.repository

import io.github.wildblazz.reaction_service.entity.Reaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface ReactionRepository : JpaRepository<Reaction, Long> {
    fun findByUserIdAndTargetUserId(userId: String, targetUserId: String): Reaction?
    fun findByIdAndUserId(reactionId: Long, userId: String): Optional<Reaction>
    fun findByUserIdAndCreatedAtBetween(
        userId: String, startTime: LocalDateTime, endTime: LocalDateTime, pageable: Pageable
    ): Page<Reaction>
    fun deleteAllByUserIdOrTargetUserId(userId: String, targetUserId: String)
}
