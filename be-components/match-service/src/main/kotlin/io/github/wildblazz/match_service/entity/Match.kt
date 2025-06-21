package io.github.wildblazz.match_service.entity

import io.github.wildblazz.match_service.model.MatchStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@CompoundIndex(name = "unique_match_idx", def = "{'user1': 1, 'user2': 1}", unique = true)
@CompoundIndex(name = "users_match_idx", def = "{'user1': 1, 'user2': 1,  'status': 1}", unique = true)
@CompoundIndex(name = "last_activity_idx", def = "{'lastActivity': -1}")
@Document(collection = "matches")
data class Match(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val user1: String,
    val user2: String,
    var status: MatchStatus = MatchStatus.CREATED,
    var lastActivity: Instant? = null,
    var matchedAt: Instant? = null
)
