package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.ProfileDto
import java.util.UUID

interface ProfileService {
    fun getProfileById(id: UUID): ProfileDto
    fun getProfileByUserId(userId: String): ProfileDto
    fun createProfile(profileDto: ProfileDto, userId: String): ProfileDto
    fun updateProfile(id: UUID, profileDto: ProfileDto, userId: String): ProfileDto
    fun deleteProfile(id: UUID, userId: String)
    fun searchProfiles(age: Int?, gender: String?, location: String?, page: Int, size: Int): List<ProfileDto>
}
