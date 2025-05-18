package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.*
import org.springframework.data.domain.Page

interface ProfileService {
    fun getProfileByKeycloakId(keycloakId: String): ProfileDto
    fun createProfile(profileDto: CreateProfileDto): ProfileDto
    fun updateProfile(keycloakId: String, profileDto: UpdateProfileDto): ProfileDto
    fun deleteProfile(keycloakId: String)
    fun searchProfiles(criteria: SearchCriteria, page: Int, size: Int): Page<ProfileDto>
    fun updateUserRole(userId: String, roleDto: UpdateRoleDto)
    fun isProfileExistsByEmail(email: String)
}
