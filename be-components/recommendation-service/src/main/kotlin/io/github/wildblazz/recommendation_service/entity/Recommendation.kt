package io.github.wildblazz.recommendation_service.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "recommendation")
data class Recommendation(
    @Id
    val recommendationId: String? = null,
    val userId: String,
    val recommendedUserIds: List<String>,
    val generatedAt: Instant = Instant.now(),
    var viewed: Boolean = false
)
