package io.github.wildblazz.profile_service.model.dto

import java.util.*

data class PhotoDto(
    val id: UUID,
    val url: String,
    val isMain: Boolean
)
