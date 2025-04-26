package io.github.wildblazz.common.exception

class StorageException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.common.storage", messageKey, args, cause)
