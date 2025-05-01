package io.github.wildblazz.reaction_service.model.dto

import io.github.wildblazz.reaction_service.model.ReactionType
import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateRequest(
    @field:NotNull
    val targetUserId: UUID,
    @field:NotNull
    val reactionType: ReactionType,

    val isSuper: Boolean = false
)
