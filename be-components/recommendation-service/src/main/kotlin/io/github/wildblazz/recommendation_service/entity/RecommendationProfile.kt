package io.github.wildblazz.recommendation_service.entity

import io.github.wildblazz.shared.model.Gender
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "recommendation_profile")
data class RecommendationProfile(
    @Id
    val keycloakId: String,
    val userName: String,
    var firstName: String,
    var lastName: String,
    var latitude: Double,
    var longitude: Double,
    var city: String,
    var searchRadiusKm: Int = 30,
    val gender: Gender,
    var interests: List<String>,
    var lastUpdatedAt: Instant = Instant.now()
)
