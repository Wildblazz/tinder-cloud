package io.github.wildblazz.profile_service.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {
    val <T : Any> T.logger: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(e: AccessDeniedException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = "Access denied: ${e.message}",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDenied(e: AuthorizationDeniedException): ResponseEntity<String> {
        logger.warn(">>> Global handler: handleAuthorizationDenied - {}", e.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: ${e.message}")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthException(e: AuthenticationException): ResponseEntity<String> {
        logger.warn(">>> Global handler: AuthenticationException - {}", e.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: ${e.message}")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleProfileNotFoundException(
        ex: NotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Profile not found",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariableException(
        ex: MissingPathVariableException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Path is not found",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PhotoNotFoundException::class)
    fun handlePhotoNotFoundException(ex: PhotoNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Photo not found",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedAccessException(
        ex: UnauthorizedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = ex.message ?: "Unauthorized access",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(StorageException::class)
    fun handleStorageException(ex: StorageException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message ?: "Storage error occurred",
            timestamp = System.currentTimeMillis()
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleAny(e: Exception): ResponseEntity<String> {
        logger.error(">>> Global handler: Uncaught exception", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled: ${e.message}")
    }
}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: Long
)
