package io.github.wildblazz.reaction_service.controller

import io.github.wildblazz.reaction_service.entity.Reaction
import io.github.wildblazz.reaction_service.model.dto.CreateRequest
import io.github.wildblazz.reaction_service.service.ReactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Validated
@RestController
@RequestMapping("/api/reactions/{userId}")
@Tag(name = "Reactions", description = "Reactions controller")
class ReactionController(private val reactionService: ReactionService) {

    @PostMapping
    @PreAuthorize("#userId == authentication.name")
    @Operation(summary = "Create a new reaction")
    fun createReaction(
        @PathVariable userId: String, @Valid @RequestBody request: CreateRequest
    ): ResponseEntity<Reaction> {
        val reaction = reactionService.createReaction(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(reaction)
    }

    @GetMapping("/list")
    @Operation(summary = "Get all user reactions with pagination and timeframes")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun getReactionsByUserId(
        @PathVariable userId: String,
        @RequestParam(required = false) startTime: LocalDateTime?,
        @RequestParam(required = false) endTime: LocalDateTime?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Reaction>> {
        val pageable = PageRequest.of(page, size)
        val reactions = reactionService.getReactionsByUserId(userId, startTime, endTime, pageable)
        return ResponseEntity.ok(reactions)
    }

    @DeleteMapping("/{reactionId}")
    @Operation(summary = "Delete reaction")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun deleteReaction(
        @PathVariable userId: String, @PathVariable reactionId: Long
    ): ResponseEntity<Void> {
        reactionService.deleteReaction(userId, reactionId)
        return ResponseEntity.noContent().build()
    }
}
