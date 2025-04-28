package io.github.wildblazz.profile_service.exception

import io.github.wildblazz.shared.exception.util.ErrorDetails
import io.github.wildblazz.shared.exception.util.MessageUtil
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource

) {
    @ExceptionHandler(PhotoNotFoundException::class)
    fun handlePhotoNotFoundException(ex: PhotoNotFoundException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorMessage = this.getMessage(ex.messageKey, ex.getDefaultMessageKey(), ex.getArgs())
        return ResponseEntity<ErrorDetails>(ErrorDetails(errorMessage), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StorageException::class)
    fun handleStorageException(ex: StorageException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorMessage = this.getMessage(ex.messageKey, ex.getDefaultMessageKey(), ex.getArgs())
        return ResponseEntity<ErrorDetails>(ErrorDetails(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun getMessage(messageKey: String?, defaultMessageKey: String, args: Array<Any?>?): String {
        return MessageUtil.getMessage(messageSource, messageKey, defaultMessageKey, args)
    }
}
