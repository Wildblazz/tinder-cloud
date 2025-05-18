package io.github.wildblazz.profile_service.controller

import io.github.wildblazz.profile_service.model.dto.*
import io.github.wildblazz.profile_service.service.ProfileService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@Validated
@RestController
@RequestMapping("/api/profiles")
class ProfileController(private val profileService: ProfileService) {

    @GetMapping("/{userId}")
    fun getProfile(@PathVariable userId: String): ResponseEntity<ProfileDto> {
        val profile = profileService.getProfileByKeycloakId(userId)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/me")
    fun getCurrentUserProfile(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<ProfileDto> {
        val profile = profileService.getProfileByKeycloakId(jwt.subject)
        return ResponseEntity.ok(profile)
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    fun createProfile(
        @RequestBody @Valid profileDto: CreateProfileDto,
    ): ResponseEntity<ProfileDto> {
        val savedProfile = profileService.createProfile(profileDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile)
    }

    @PostMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateRole(
        @RequestBody @Valid roleDto: UpdateRoleDto,
        @PathVariable userId: String
    ): ResponseEntity<Unit> {
        profileService.updateUserRole(userId, roleDto)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun updateProfile(
        @PathVariable userId: String,
        @RequestBody @Valid profileDto: UpdateProfileDto,
    ): ResponseEntity<ProfileDto> {
        val updatedProfile = profileService.updateProfile(userId, profileDto)
        return ResponseEntity.ok(updatedProfile)
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    fun deleteProfile(
        @PathVariable userId: String
    ): ResponseEntity<Unit> {
        profileService.deleteProfile(userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchProfiles(
        @RequestParam(required = false) age: Int?,
        @RequestParam(required = false) gender: String?,
        @RequestParam(required = false) location: String?,
        @RequestParam(required = false) interests: List<String>?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ProfileDto>> {
        val criteria = SearchCriteria(age = age, gender = gender, location = location, interests = interests)
        val profiles = profileService.searchProfiles(criteria, page, size)
        return ResponseEntity.ok(profiles)
    }
}

