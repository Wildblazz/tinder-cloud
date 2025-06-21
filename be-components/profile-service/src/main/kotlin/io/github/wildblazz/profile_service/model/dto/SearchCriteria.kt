package io.github.wildblazz.profile_service.model.dto

data class SearchCriteria(
    val age: Int? = null,
    val gender: String? = null,
    val location: String? = null,
    val interests: List<String>? = null
)
