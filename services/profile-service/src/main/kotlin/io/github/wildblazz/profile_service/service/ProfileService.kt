package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import io.github.wildblazz.profile_service.model.dto.ProfileDto

interface ProfileService {
    fun getProfileByUserId(userId: String): ProfileDto
    fun createProfile(profileDto: CreateProfileDto): ProfileDto
    fun updateProfile(userId: String, profileDto: ProfileDto): ProfileDto
    fun deleteProfile(userId: String)
    fun searchProfiles(age: Int?, gender: String?, location: String?, page: Int, size: Int): List<ProfileDto>
}
