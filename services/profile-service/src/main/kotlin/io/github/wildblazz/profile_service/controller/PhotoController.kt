package io.github.wildblazz.profile_service.controller

import io.github.wildblazz.profile_service.model.dto.PhotoDto
import io.github.wildblazz.profile_service.service.PhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/profiles/{profileId}/photos")
class PhotoController(private val photoService: PhotoService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @PathVariable profileId: UUID,
        @RequestParam("file") file: MultipartFile,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<PhotoDto> {
        val userId = jwt.subject
        val photoDto = photoService.uploadPhoto(profileId, file, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(photoDto)
    }

    @GetMapping
    fun getPhotos(
        @PathVariable profileId: UUID
    ): ResponseEntity<List<PhotoDto>> {
        val photos = photoService.getPhotosByProfileId(profileId)
        return ResponseEntity.ok(photos)
    }

    @DeleteMapping("/{photoId}")
    fun deletePhoto(
        @PathVariable profileId: UUID,
        @PathVariable photoId: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        photoService.deletePhoto(profileId, photoId, userId)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{photoId}/set-main")
    fun setMainPhoto(
        @PathVariable profileId: UUID,
        @PathVariable photoId: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        photoService.setMainPhoto(profileId, photoId, userId)
        return ResponseEntity.ok().build()
    }
}
