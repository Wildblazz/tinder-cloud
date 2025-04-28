package io.github.wildblazz.shared.exception.types

class DuplicateException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.duplicate", messageKey, args, cause)
