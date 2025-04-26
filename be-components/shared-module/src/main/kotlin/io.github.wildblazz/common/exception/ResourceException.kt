package io.github.wildblazz.common.exception

abstract class ResourceException(
    private val defaultMessageKey: String,
    private val messageKey: String? = null,
    private val args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : Exception(messageKey, cause) {

    open fun getArgs(): Array<Any?> = args.clone()

    fun getDefaultMessageKey(): String = defaultMessageKey
}

