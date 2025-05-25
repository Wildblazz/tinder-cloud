package io.github.wildblazz.recommendation_service.controller

import io.github.wildblazz.recommendation_service.entity.Recommendation
import io.github.wildblazz.recommendation_service.service.RecommendationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/recommendation")
class RecommendationController(private val recommendationService: RecommendationService) {

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun getRecommendation(@PathVariable userId: String): ResponseEntity<Recommendation> {
        val recommendation = recommendationService.getRecommendationForUser(userId)
        return ResponseEntity.ok(recommendation)
    }
}
