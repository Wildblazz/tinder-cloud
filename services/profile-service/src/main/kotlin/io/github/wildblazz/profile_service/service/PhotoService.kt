package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.PhotoDto
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface PhotoService {
    fun uploadPhoto(profileId: UUID, file: MultipartFile, userId: String): PhotoDto
    fun getPhotosByProfileId(profileId: UUID): List<PhotoDto>
    fun deletePhoto(profileId: UUID, photoId: UUID, userId: String)
    fun setMainPhoto(profileId: UUID, photoId: UUID, userId: String)
}
