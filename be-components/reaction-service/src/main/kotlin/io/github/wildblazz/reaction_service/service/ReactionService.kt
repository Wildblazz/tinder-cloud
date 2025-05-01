package io.github.wildblazz.reaction_service.service

import io.github.wildblazz.reaction_service.common.Constants
import io.github.wildblazz.reaction_service.entity.Reaction
import io.github.wildblazz.reaction_service.model.dto.CreateRequest
import io.github.wildblazz.reaction_service.repository.ReactionRepository
import io.github.wildblazz.shared.exception.types.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class ReactionService(
    private val reactionRepository: ReactionRepository
) {
    @Transactional
    fun createReaction(userId: String, request: CreateRequest): Reaction {
        val userIdUuid = UUID.fromString(userId)


        val existingReaction = reactionRepository.findByUserIdAndTargetUserId(userIdUuid, request.targetUserId)
        if (existingReaction != null) {
            return existingReaction
        }

        val reaction = Reaction(
            id = null,
            userId = userIdUuid,
            targetUserId = request.targetUserId,
            type = request.reactionType,
            isSuper = request.isSuper
        )

        return reactionRepository.save(reaction)
    }

    @Transactional(readOnly = true)
    fun getReactionsByUserId(
        userId: UUID, startTime: LocalDateTime?, endTime: LocalDateTime?, pageable: Pageable
    ): Page<Reaction> {
        return reactionRepository.findByUserIdAndCreatedAtBetween(
            userId, startTime ?: LocalDateTime.MIN, endTime ?: LocalDateTime.MAX, pageable
        )
    }

    fun deleteReaction(userId: UUID, id: Long) {
        val reaction = getReactionById(userId, id)
        reactionRepository.delete(reaction)
    }

    private fun getReactionById(userId: UUID, id: Long): Reaction {
        return reactionRepository.findByIdAndUserId(id, userId)
            .orElseThrow { NotFoundException(Constants.MESSAGE_REACTION_NOT_FOUND, arrayOf(id)) }
    }
}
