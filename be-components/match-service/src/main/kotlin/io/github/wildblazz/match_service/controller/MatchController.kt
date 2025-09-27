package io.github.wildblazz.match_service.controller

import io.github.wildblazz.match_service.entity.Match
import io.github.wildblazz.match_service.service.MatchService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/matches")
class MatchController(private val matchService: MatchService) {

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun getMatches(@PathVariable userId: String): ResponseEntity<List<Match>> {
        val matches = matchService.getMatchesForUser(userId)
        return ResponseEntity.ok(matches)
    }

    @GetMapping("/exists")
    @PreAuthorize("hasRole('ADMIN') or #userA == authentication.name or #userB == authentication.name")
    fun matchExists(@RequestParam userA: String, @RequestParam userB: String): ResponseEntity<Void> {
        val exists = matchService.matchExists(userA, userB)
        return if (exists) ResponseEntity.ok().build() else ResponseEntity.notFound().build()
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or #userA == authentication.name or #userB == authentication.name")
    fun deleteMatch(@RequestParam userA: String, @RequestParam userB: String): ResponseEntity<Void> {
        val deleted = matchService.deleteMatch(userA, userB)
        return if (deleted) ResponseEntity.ok().build() else ResponseEntity.notFound().build()
    }
}
