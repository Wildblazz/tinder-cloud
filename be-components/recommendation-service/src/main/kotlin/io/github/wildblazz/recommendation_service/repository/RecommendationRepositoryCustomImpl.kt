package io.github.wildblazz.recommendation_service.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class RecommendationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : RecommendationRepositoryCustom {

    override fun findUsersNeedingRecommendations(oneDayAgo: Instant, limit: Int): List<String> {
        val match = match(
            Criteria().orOperator(
                Criteria().andOperator(
                    Criteria("viewed").`is`(false),
                    Criteria("generatedAt").gte(oneDayAgo)
                ).not()
            )
        )
        val group = group("userId")
        val sort = sort(Sort.Direction.ASC, "userId")
        val limitStage = limit(limit.toLong())

        val aggregation = newAggregation(match, group, sort, limitStage)
        val results = mongoTemplate.aggregate(aggregation, "recommendation", Map::class.java)
        return results.mappedResults.map { it["_id"].toString() }
    }
}
