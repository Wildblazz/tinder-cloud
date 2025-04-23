package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.CreateProfileDto
import io.github.wildblazz.profile_service.model.dto.ProfileDto
import io.github.wildblazz.profile_service.model.dto.SearchCriteria
import io.github.wildblazz.profile_service.model.dto.UpdateProfileDto
import org.springframework.data.domain.Page

interface ProfileService {
    fun getProfileByUserId(userId: String): ProfileDto
    fun createProfile(profileDto: CreateProfileDto): ProfileDto
    fun updateProfile(userId: String, profileDto: UpdateProfileDto): ProfileDto
    fun deleteProfile(userId: String)
    fun searchProfiles(criteria: SearchCriteria, page: Int, size: Int): Page<ProfileDto>
}
