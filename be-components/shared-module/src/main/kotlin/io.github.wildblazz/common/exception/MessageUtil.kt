package io.github.wildblazz.common.exception

import io.github.wildblazz.common.constants.Constants
import org.springframework.context.MessageSource
import java.util.*

object MessageUtil {
    fun getMessage(
        messageSource: MessageSource,
        messageKey: String?,
        defaultMessageKey: String,
        args: Array<Any?>?
    ): String {
        return try {
            messageSource.getMessage(messageKey ?: defaultMessageKey, args, Locale.ROOT)
        } catch (e: Exception) {
            messageSource.getMessage(defaultMessageKey, args, Locale.ROOT)
        }
    }

    fun getMessage(messageSource: MessageSource, messageKey: String): String {
        return try {
            messageSource.getMessage(messageKey, null, Locale.ROOT)
        } catch (e: Exception) {
            messageSource.getMessage(Constants.KEY_MESSAGE_NOT_FOUND, arrayOf(messageKey), Locale.ROOT)
        }
    }
}
