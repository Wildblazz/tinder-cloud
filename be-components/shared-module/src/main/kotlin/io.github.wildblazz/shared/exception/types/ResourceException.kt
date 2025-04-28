package io.github.wildblazz.shared.exception.types

abstract class ResourceException(
    private val defaultMessageKey: String,
    private val messageKey: String? = null,
    private val args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : Exception(messageKey, cause) {

    open fun getArgs(): Array<Any?> = args.clone()

    fun getDefaultMessageKey(): String = defaultMessageKey
}

