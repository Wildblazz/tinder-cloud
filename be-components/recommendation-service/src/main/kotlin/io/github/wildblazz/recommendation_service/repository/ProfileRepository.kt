package io.github.wildblazz.recommendation_service.repository

import io.github.wildblazz.recommendation_service.entity.RecommendationProfile
import org.springframework.data.mongodb.repository.MongoRepository

interface ProfileRepository : MongoRepository<RecommendationProfile, String>
