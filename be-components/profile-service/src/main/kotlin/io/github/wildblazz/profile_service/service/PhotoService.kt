package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.PhotoDto
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface PhotoService {
    fun uploadPhoto(userId: String, file: MultipartFile): PhotoDto
    fun getPhotosByProfileId(userId: String): List<PhotoDto>
    fun deletePhoto(userId: String, photoId: UUID)
    fun setMainPhoto(userId: String, photoId: UUID)
}
