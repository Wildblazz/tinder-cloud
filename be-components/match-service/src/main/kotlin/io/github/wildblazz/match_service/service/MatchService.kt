package io.github.wildblazz.match_service.service

import io.github.wildblazz.match_service.entity.Match
import io.github.wildblazz.match_service.model.MatchStatus
import io.github.wildblazz.match_service.repository.MatchRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MatchService(private val matchRepository: MatchRepository) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun createMatch(userA: String, userB: String) {
        val (fromUser, toUser) = listOf(userA, userB).sorted()
        val existingEntity = matchRepository.findByUser1AndUser2(fromUser, toUser)
        if (existingEntity.isPresent) {
            val existingMatch = existingEntity.get()
            val now = Instant.now()
            existingMatch.matchedAt = now
            existingMatch.lastActivity = now
            existingMatch.status = MatchStatus.MATCHED
            matchRepository.save(existingMatch)
            logger.info("Updated match ${existingMatch.user1} for ${existingMatch.user2}")
            // send notification event

            return;
        }
        val match = Match(
            user1 = fromUser,
            user2 = toUser,
            status = MatchStatus.CREATED,
        )
        matchRepository.save(match)
        logger.info("Created new match ${match.user1} for ${match.user2}")

    }

    fun getMatchesForUser(userId: String): List<Match> {
        return matchRepository.findByUser1OrUser2AndStatus(userId, userId, MatchStatus.MATCHED)
    }

    fun matchExists(userA: String, userB: String): Boolean {
        val (fromUser, toUser) = listOf(userA, userB).sorted()
        return matchRepository.findByUser1AndUser2AndStatus(fromUser, toUser, MatchStatus.MATCHED)
            .isPresent
    }

    fun deleteMatch(userA: String, userB: String): Boolean {
        val (fromUser, toUser) = listOf(userA, userB).sorted()
        var deleted = false
        matchRepository.findByUser1AndUser2(fromUser, toUser).ifPresent { match ->
            matchRepository.delete(match)
            deleted = true
        }
        return deleted
    }

    fun deleteMatchesByUserId(userId: String) {
        val matches = matchRepository.findByUser1OrUser2(userId, userId)
        matchRepository.deleteAllById(matches.map { it.id })
        logger.info("Deleted matches for user $userId")
    }
}
