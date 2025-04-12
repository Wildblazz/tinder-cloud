package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.exception.PhotoNotFoundException
import io.github.wildblazz.profile_service.exception.ProfileNotFoundException
import io.github.wildblazz.profile_service.exception.UnauthorizedAccessException
import io.github.wildblazz.profile_service.model.Photo
import io.github.wildblazz.profile_service.model.dto.PhotoDto
import io.github.wildblazz.profile_service.repository.PhotoRepository
import io.github.wildblazz.profile_service.repository.ProfileRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class PhotoServiceImpl(
    private val photoRepository: PhotoRepository,
    private val profileRepository: ProfileRepository,
    private val storageService: StorageService
) : PhotoService {

    @Transactional
    override fun uploadPhoto(profileId: UUID, file: MultipartFile, userId: String): PhotoDto {
        val profile = profileRepository.findByIdOrNull(profileId)
            ?: throw ProfileNotFoundException("Profile with id $profileId not found")

        if (profile.userId != userId) {
            throw UnauthorizedAccessException("You don't have permission to upload photos to this profile")
        }

        val fileName = "${UUID.randomUUID()}-${file.originalFilename}"
        val url = storageService.uploadFile(fileName, file.inputStream, file.contentType ?: "image/jpeg")

        val isMain = photoRepository.countByProfileId(profileId) == 0L

        val photo = Photo(
            id = UUID.randomUUID(),
            profile = profile,
            url = url,
            isMain = isMain
        )

        val savedPhoto = photoRepository.save(photo)
        return mapToDto(savedPhoto)
    }

    override fun getPhotosByProfileId(profileId: UUID): List<PhotoDto> {
        if (!profileRepository.existsById(profileId)) {
            throw ProfileNotFoundException("Profile with id $profileId not found")
        }

        val photos = photoRepository.findByProfileIdOrderByIsMainDesc(profileId)
        return photos.map { mapToDto(it) }
    }

    @Transactional
    override fun deletePhoto(profileId: UUID, photoId: UUID, userId: String) {
        val profile = profileRepository.findByIdOrNull(profileId)
            ?: throw ProfileNotFoundException("Profile with id $profileId not found")

        if (profile.userId != userId) {
            throw UnauthorizedAccessException("You don't have permission to delete photos from this profile")
        }

        val photo = photoRepository.findByIdAndProfileId(photoId, profileId)
            ?: throw PhotoNotFoundException("Photo with id $photoId not found for profile $profileId")

        val fileName = photo.url.substringAfterLast("/")
        storageService.deleteFile(fileName)

        photoRepository.delete(photo)

        if (photo.isMain) {
            val nextPhoto = photoRepository.findFirstByProfileId(profileId)
            nextPhoto?.let {
                it.isMain = true
                photoRepository.save(it)
            }
        }
    }

    @Transactional
    override fun setMainPhoto(profileId: UUID, photoId: UUID, userId: String) {
        val profile = profileRepository.findByIdOrNull(profileId)
            ?: throw ProfileNotFoundException("Profile with id $profileId not found")

        if (profile.userId != userId) {
            throw UnauthorizedAccessException("You don't have permission to modify photos for this profile")
        }

        val newMainPhoto = photoRepository.findByIdAndProfileId(photoId, profileId)
            ?: throw PhotoNotFoundException("Photo with id $photoId not found for profile $profileId")

        val currentMainPhoto = photoRepository.findByProfileIdAndIsMainTrue(profileId)
        currentMainPhoto?.let {
            it.isMain = false
            photoRepository.save(it)
        }

        newMainPhoto.isMain = true
        photoRepository.save(newMainPhoto)
    }

    private fun mapToDto(photo: Photo): PhotoDto {
        return PhotoDto(
            id = photo.id,
            url = photo.url,
            isMain = photo.isMain
        )
    }
}
