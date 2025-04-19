package io.github.wildblazz.profile_service.repository

import io.github.wildblazz.profile_service.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PhotoRepository : JpaRepository<Photo, UUID> {
    fun findByProfileIdOrderByIsMainDesc(profileId: UUID): List<Photo>
    fun findByIdAndProfileId(photoId: UUID, profileId: UUID): Photo?
    fun findByProfileIdAndIsMainTrue(profileId: UUID): Photo?
    fun countByProfileId(profileId: UUID): Long
    fun findFirstByProfileId(profileId: UUID): Photo?
}
