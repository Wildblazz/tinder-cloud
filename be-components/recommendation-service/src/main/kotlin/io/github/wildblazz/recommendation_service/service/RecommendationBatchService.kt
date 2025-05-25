package io.github.wildblazz.recommendation_service.service

import io.github.wildblazz.recommendation_service.repository.ProfileRepository
import io.github.wildblazz.recommendation_service.repository.RecommendationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class RecommendationBatchService(
    private val profileRepository: ProfileRepository,
    private val recommendationRepository: RecommendationRepository,
    private val recommendationService: RecommendationService
) {

    @Scheduled(cron = "\${recommendation.batch-generate.cron}")
    fun processBatch() {
        val oneDayAgo = Instant.now().minus(Duration.ofDays(1))

        val usersToProcess = recommendationRepository.findUsersNeedingRecommendations(oneDayAgo, 50)
        val users = profileRepository.findAllById(usersToProcess)

        users.forEach { user ->
            recommendationService.generateRecommendationsForUser(user)
        }
    }
}
