package io.github.wildblazz.recommendation_service.repository

import java.time.Instant

interface RecommendationRepositoryCustom {
    fun findUsersNeedingRecommendations(oneDayAgo: Instant, limit: Int): List<String>
}
