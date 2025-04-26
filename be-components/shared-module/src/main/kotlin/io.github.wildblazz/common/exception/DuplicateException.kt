package io.github.wildblazz.common.exception

class DuplicateException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.duplicate", messageKey, args, cause)
