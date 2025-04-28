package io.github.wildblazz.shared.exception.types

class NotFoundException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.not.found", messageKey, args, cause)
