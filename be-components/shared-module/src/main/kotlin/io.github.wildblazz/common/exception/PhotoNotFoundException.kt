package io.github.wildblazz.common.exception

class PhotoNotFoundException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.common.photo.not.found", messageKey, args, cause) {
}
