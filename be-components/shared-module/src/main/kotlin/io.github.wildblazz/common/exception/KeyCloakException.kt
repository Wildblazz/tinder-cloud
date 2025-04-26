package io.github.wildblazz.common.exception

class KeyCloakException(
    val messageKey: String?,
    args: Array<Any?> = emptyArray(),
    cause: Throwable? = null
) : ResourceException("exception.keycloak.generic", messageKey, args, cause)
