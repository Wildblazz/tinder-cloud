package io.github.wildblazz.common.exception

class NotFoundException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.not.found", messageKey, args, cause)
