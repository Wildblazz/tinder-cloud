package io.github.wildblazz.recommendation_service.service

import io.github.wildblazz.recommendation_service.common.Constants
import io.github.wildblazz.recommendation_service.entity.Recommendation
import io.github.wildblazz.recommendation_service.entity.RecommendationProfile
import io.github.wildblazz.recommendation_service.repository.ProfileRepository
import io.github.wildblazz.recommendation_service.repository.RecommendationRepository
import io.github.wildblazz.shared.event.model.ProfileUpdateEvent
import io.github.wildblazz.shared.exception.types.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class RecommendationService(
    private val profileRepository: ProfileRepository,
    private val recommendationRepository: RecommendationRepository,
    private val mongoTemplate: MongoTemplate
) {
    fun getRecommendationForUser(userId: String): Recommendation {
        val profile = profileRepository.findById(userId)
            .orElseThrow { NotFoundException(Constants.RECOMMENDATION_PROFILE_NOT_FOUND, arrayOf(userId)) }
        val allRecommendations = recommendationRepository.findByUserId(profile.keycloakId)
        val recommendation = allRecommendations.firstOrNull {
            !it.viewed && it.generatedAt.isAfter(Instant.now().minus(Duration.ofDays(1)))
        } ?: generateRecommendationsForUser(profile)
        recommendation.viewed = true
        return recommendationRepository.save(recommendation)

    }

    fun generateRecommendationsForUser(user: RecommendationProfile): Recommendation {
        var allRecommendations = recommendationRepository.findByUserId(user.keycloakId)

        allRecommendations.firstOrNull {
            !it.viewed && it.generatedAt.isAfter(Instant.now().minus(Duration.ofDays(1)))
        }?.let { return it }

        if (allRecommendations.size > 3) {
            val toDelete = allRecommendations
                .sortedBy { it.generatedAt }
                .take(allRecommendations.size - 3)
            recommendationRepository.deleteAll(toDelete)
            val toDeleteIds = toDelete.map { it.recommendationId }.toSet()
            allRecommendations = allRecommendations.filterNot { it.recommendationId in toDeleteIds }
        }

        val viewedRecommendations = allRecommendations
            .flatMap { it.recommendedUserIds }
            .toSet()

        val geoCriteria = Criteria.where("location")
            .nearSphere(user.location)
            .maxDistance(kmToRadians(user.searchRadiusKm))

        val interestCriteria = Criteria.where("interests").`in`(user.interests)
        val genderCriteria = Criteria.where("gender").ne(user.gender)
        val idCriteria = Criteria.where("keycloakId").ne(user.keycloakId)
        val excludeViewedCriteria = viewedRecommendations.takeIf { it.isNotEmpty() }
            ?.let { Criteria.where("keycloakId").nin(it) }

        val criteriaList = listOfNotNull(
            geoCriteria, interestCriteria, genderCriteria, idCriteria, excludeViewedCriteria
        )
        val finalCriteria = Criteria().andOperator(*criteriaList.toTypedArray())

        val query = Query(finalCriteria)
            .limit(50)
            .with(Sort.by(Sort.Direction.DESC, "lastUpdatedAt"))

        val results = mongoTemplate.find(query, RecommendationProfile::class.java)
        val ids = results.map { it.keycloakId }

        return recommendationRepository.save(Recommendation(userId = user.keycloakId, recommendedUserIds = ids))
    }

    private fun kmToRadians(km: Int): Double = km / 6378.1


    fun saveNewProfile(recommendationProfile: RecommendationProfile) {
        val profile = profileRepository.save(recommendationProfile)
        generateRecommendationsForUser(profile)

    }

    fun removeProfile(keycloakId: String) {
        profileRepository.deleteById(keycloakId)
        recommendationRepository.deleteAllByUserId(keycloakId)
    }

    fun updateProfile(event: ProfileUpdateEvent) {
        var regenerationRequire = false
        val profile = profileRepository.findById(event.keycloakId).orElse(null)
        profile?.let {
            if (it.interests != event.interests ||
                it.location.x != event.longitude ||
                it.location.y != event.latitude ||
                it.searchRadiusKm != event.searchRadiusKm
            ) {
                regenerationRequire = true
            }
            it.firstName = event.firstName
            it.lastName = event.lastName
            it.searchRadiusKm = event.searchRadiusKm
            it.interests = event.interests
            it.city = event.city
            it.location = Point(event.longitude, event.latitude)
            it.lastUpdatedAt = Instant.now()
            profileRepository.save(it)
            if (regenerationRequire) {
                generateRecommendationsForUser(it)
            }
        }
    }
}
