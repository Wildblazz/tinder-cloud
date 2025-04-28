package io.github.wildblazz.profile_service.service

import io.github.wildblazz.shared.exception.types.NotFoundException
import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.common.Constants.EXCEPTION_PROFILE_NOT_FOUND
import io.github.wildblazz.profile_service.exception.PhotoNotFoundException
import io.github.wildblazz.profile_service.exception.StorageException
import io.github.wildblazz.profile_service.model.Photo
import io.github.wildblazz.profile_service.model.Profile
import io.github.wildblazz.profile_service.model.dto.PhotoDto
import io.github.wildblazz.profile_service.repository.PhotoRepository
import io.github.wildblazz.profile_service.repository.ProfileRepository
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
    override fun uploadPhoto(userId: String, file: MultipartFile): PhotoDto {
        validateFileType(file)
        val profile = getUserProfile(userId)

        val photoCount = photoRepository.countByProfileId(profile.id)
        if (photoCount >= 5L) throw StorageException(Constants.EXCEPTION_STORAGE_EXCEEDED)


        val fileName = "${UUID.randomUUID()}-${profile.id}"
        val url = storageService.uploadFile(fileName, file.inputStream, file.contentType ?: "image/jpeg")


        val isMain = photoCount == 0L

        val photo = Photo(
            id = UUID.randomUUID(),
            profile = profile,
            url = url,
            isMain = isMain
        )

        val savedPhoto = photoRepository.save(photo)
        return mapToDto(savedPhoto)
    }

    override fun getPhotosByProfileId(userId: String): List<PhotoDto> {
        val profile = getUserProfile(userId)

        val photos = photoRepository.findByProfileIdOrderByIsMainDesc(profile.id)
        return photos.map { mapToDto(it) }
    }

    @Transactional
    override fun deletePhoto(userId: String, photoId: UUID) {
        val profile = getUserProfile(userId)

        val photo = getPhoto(photoId, profile.id)

        val fileName = photo.url.substringAfterLast("/")
        storageService.deleteFile(fileName)

        photoRepository.delete(photo)

        if (photo.isMain) {
            val nextPhoto = photoRepository.findFirstByProfileId(profile.id)
            nextPhoto?.let {
                it.isMain = true
                photoRepository.save(it)
            }
        }
    }

    @Transactional
    override fun setMainPhoto(userId: String, photoId: UUID) {
        val profile = getUserProfile(userId)

        val newMainPhoto = getPhoto(photoId, profile.id)

        val currentMainPhoto = photoRepository.findByProfileIdAndIsMainTrue(profile.id)
        currentMainPhoto?.let {
            it.isMain = false
            photoRepository.save(it)
        }

        newMainPhoto.isMain = true
        photoRepository.save(newMainPhoto)
    }

    private fun getUserProfile(userId: String): Profile {
        return profileRepository.findByUserId(userId) ?: throw NotFoundException(
            EXCEPTION_PROFILE_NOT_FOUND,
            arrayOf(userId)
        )
    }

    private fun mapToDto(photo: Photo): PhotoDto {
        return PhotoDto(
            id = photo.id,
            url = photo.url,
            isMain = photo.isMain
        )
    }

    private fun getPhoto(photoId: UUID, userId: UUID): Photo {
        return photoRepository.findByIdAndProfileId(photoId, userId)
            ?: throw PhotoNotFoundException(Constants.EXCEPTION_PHOTO_NOT_FOUND, arrayOf(photoId, userId))
    }

    private fun validateFileType(file: MultipartFile) {
//        TODO move allowed types to app.yaml
        val allowedContentTypes = listOf("image/jpeg", "image/jpg", "image/png", "image/gif")
        if (file.contentType !in allowedContentTypes) {
            throw IllegalArgumentException(Constants.EXCEPTION_ILLEGAL_FILE_TYPES)
        }
    }
}
