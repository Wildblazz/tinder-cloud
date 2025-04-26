package io.github.wildblazz.common.exception

import io.github.wildblazz.common.constants.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.*

@RestControllerAdvice
class SharedGlobalExceptionHandler(
    private val messageSource: MessageSource

) {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleResourceNotFoundException(ex: NotFoundException): ResponseEntity<ErrorDetails> {
        val errorMessage = this.getMessage(ex.messageKey, ex.getDefaultMessageKey(), ex.getArgs())
        return ResponseEntity<ErrorDetails>(ErrorDetails(errorMessage), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedAccessException(
        ex: UnauthorizedException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        val errorMessage = this.getMessage(ex.messageKey, ex.getDefaultMessageKey(), ex.getArgs())
        return ResponseEntity<ErrorDetails>(ErrorDetails(errorMessage), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(KeyCloakException::class)
    fun handleKeyCloakException(ex: KeyCloakException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorMessage = this.getMessage(ex.messageKey, ex.getDefaultMessageKey(), ex.getArgs())
        return ResponseEntity<ErrorDetails>(ErrorDetails(errorMessage), HttpStatus.FAILED_DEPENDENCY)
    }

    //Spring exceptions
    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariableException(
        ex: MissingPathVariableException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(ex.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorDetails> {
        val errorMessage = ex.message ?: getMessage(Constants.EXCEPTION_ILLEGAL_ARGUMENT)
        return ResponseEntity(ErrorDetails(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorDetails> {
        val errorMessage = ex.message ?: getMessage(Constants.EXCEPTION_ILLEGAL_ARGUMENT)

        return ResponseEntity(ErrorDetails(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleAny(e: Exception): ResponseEntity<String> {
        logger.error(">>> Global handler: Uncaught exception", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled: ${e.message}")
    }

    private fun getMessage(messageKey: String?, defaultMessageKey: String, args: Array<Any?>?): String {
        return MessageUtil.getMessage(messageSource, messageKey, defaultMessageKey, args)
    }

    private fun getMessage(messageKey: String): String {
        return MessageUtil.getMessage(messageSource, messageKey)
    }
}
