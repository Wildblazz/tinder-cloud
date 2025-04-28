package io.github.wildblazz.profile_service.exception

import io.github.wildblazz.shared.exception.types.ResourceException

class PhotoNotFoundException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.photo.not.found", messageKey, args, cause) {
}
