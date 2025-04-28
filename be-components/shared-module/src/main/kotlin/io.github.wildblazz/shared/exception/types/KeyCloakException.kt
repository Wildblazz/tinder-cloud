package io.github.wildblazz.shared.exception.types

class KeyCloakException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.default.keycloak", messageKey, args, cause)
