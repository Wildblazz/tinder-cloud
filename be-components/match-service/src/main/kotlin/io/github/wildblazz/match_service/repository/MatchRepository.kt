package io.github.wildblazz.match_service.repository

import io.github.wildblazz.match_service.entity.Match
import io.github.wildblazz.match_service.model.MatchStatus
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface MatchRepository : MongoRepository<Match, String> {
    fun findByUser1AndUser2AndStatus(user1: String, user2: String, status: MatchStatus): Optional<Match>
    fun findByUser1OrUser2AndStatus(user1: String, user2: String, status: MatchStatus): List<Match>
    fun findByUser1OrUser2(user1: String, user2: String): List<Match>
    fun findByUser1AndUser2(user1: String, user2: String): Optional<Match>
}
