package io.github.wildblazz.recommendation_service.repository

import io.github.wildblazz.recommendation_service.entity.Recommendation
import org.springframework.data.mongodb.repository.MongoRepository

interface RecommendationRepository : MongoRepository<Recommendation, String>, RecommendationRepositoryCustom {
    fun findByUserId(userId: String): List<Recommendation>
    fun deleteAllByUserId(userId: String)
}
