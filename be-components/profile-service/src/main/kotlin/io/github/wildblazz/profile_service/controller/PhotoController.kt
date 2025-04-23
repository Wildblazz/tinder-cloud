package io.github.wildblazz.profile_service.controller

import io.github.wildblazz.profile_service.model.dto.PhotoDto
import io.github.wildblazz.profile_service.service.PhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Validated
@RestController
@RequestMapping("/api/profiles/{userId}/photos")
class PhotoController(private val photoService: PhotoService) {

    @GetMapping
    fun getPhotos(
        @PathVariable userId: String
    ): ResponseEntity<List<PhotoDto>> {
        val photos = photoService.getPhotosByProfileId(userId)
        return ResponseEntity.ok(photos)
    }

    @PreAuthorize("#userId == authentication.name")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @PathVariable userId: String,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<PhotoDto> {
        val photoDto = photoService.uploadPhoto(userId, file)
        return ResponseEntity.status(HttpStatus.CREATED).body(photoDto)
    }

    @PreAuthorize("#userId == authentication.name")
    @PutMapping("/{photoId}/set-main")
    fun setMainPhoto(
        @PathVariable userId: String,
        @PathVariable photoId: UUID
    ): ResponseEntity<Unit> {
        photoService.setMainPhoto(userId, photoId)
        return ResponseEntity.ok().build()
    }


    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    @DeleteMapping("/{photoId}")
    fun deletePhoto(
        @PathVariable userId: String,
        @PathVariable photoId: UUID
    ): ResponseEntity<Unit> {
        photoService.deletePhoto(userId, photoId)
        return ResponseEntity.noContent().build()
    }


}
