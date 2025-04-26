package io.github.wildblazz.common.exception

class UnauthorizedException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.unauthorized", messageKey, args, cause)
