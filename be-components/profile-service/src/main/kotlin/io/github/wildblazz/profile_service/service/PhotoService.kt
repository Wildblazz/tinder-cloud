package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.dto.PhotoDto
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface PhotoService {
    fun uploadPhoto(keycloakId: String, file: MultipartFile): PhotoDto
    fun getPhotosByProfileId(keycloakId: String): List<PhotoDto>
    fun deletePhoto(keycloakId: String, photoId: UUID)
    fun setMainPhoto(keycloakId: String, photoId: UUID)
}
