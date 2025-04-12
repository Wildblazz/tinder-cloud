package io.github.wildblazz.profile_service.controller

import io.github.wildblazz.profile_service.model.dto.ProfileDto
import io.github.wildblazz.profile_service.service.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/profiles")
class ProfileController(private val profileService: ProfileService) {

    @GetMapping("/{id}")
    fun getProfile(@PathVariable id: UUID): ResponseEntity<ProfileDto> {
        val profile = profileService.getProfileById(id)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/me")
    fun getCurrentUserProfile(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<ProfileDto> {
        val userId = jwt.subject
        val profile = profileService.getProfileByUserId(userId)
        return ResponseEntity.ok(profile)
    }

    @PostMapping
    fun createProfile(
        @RequestBody profileDto: ProfileDto,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<ProfileDto> {
        val userId = jwt.subject
        val savedProfile = profileService.createProfile(profileDto, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile)
    }

    @PutMapping("/{id}")
    fun updateProfile(
        @PathVariable id: UUID,
        @RequestBody profileDto: ProfileDto,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<ProfileDto> {
        val userId = jwt.subject
        val updatedProfile = profileService.updateProfile(id, profileDto, userId)
        return ResponseEntity.ok(updatedProfile)
    }

    @DeleteMapping("/{id}")
    fun deleteProfile(
        @PathVariable id: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        profileService.deleteProfile(id, userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchProfiles(
        @RequestParam(required = false) age: Int?,
        @RequestParam(required = false) gender: String?,
        @RequestParam(required = false) location: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<ProfileDto>> {
        val profiles = profileService.searchProfiles(age, gender, location, page, size)
        return ResponseEntity.ok(profiles)
    }
}

