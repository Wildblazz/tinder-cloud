package io.github.wildblazz.recommendation_service.entity

import io.github.wildblazz.shared.model.Gender
import org.springframework.data.annotation.Id
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "recommendation_profile")
data class RecommendationProfile(
    @Id
    val keycloakId: String,
    val userName: String,
    var firstName: String,
    var lastName: String,
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    var location: Point,
    var city: String,
    var searchRadiusKm: Int = 30,
    val gender: Gender,
    var interests: List<String>,
    var lastUpdatedAt: Instant = Instant.now()
)
