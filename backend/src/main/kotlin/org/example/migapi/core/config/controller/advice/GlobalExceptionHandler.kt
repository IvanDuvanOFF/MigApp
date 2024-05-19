package org.example.migapi.core.config.controller.advice

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.mail.MessagingException
import jakarta.persistence.PersistenceException
import org.example.migapi.core.config.exception.MigApplicationException
import org.example.migapi.core.domain.dto.Error
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.net.SocketException
import java.nio.file.InvalidPathException
import java.time.DateTimeException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger: KLogger = KotlinLogging.logger { }

    @ExceptionHandler(
        value = [
            MaxUploadSizeExceededException::class,
            MigApplicationException::class,
            PersistenceException::class,
            DisabledException::class,
            JwtException::class,
            ExpiredJwtException::class,
            BadCredentialsException::class,
            AuthenticationException::class,
            MessagingException::class,
            SocketException::class,
            IllegalArgumentException::class,
            DateTimeException::class,
            InvalidPathException::class,
            AccessDeniedException::class,
            Exception::class
        ]
    )
    fun processException(e: Exception): ResponseEntity<Error> {
        val status = when (e) {
            is MigApplicationException -> e.httpStatus

            is InvalidPathException,
            is PersistenceException,
            is MessagingException,
            is DateTimeException,
            is IllegalArgumentException,
            is SocketException -> HttpStatus.INTERNAL_SERVER_ERROR

            is ExpiredJwtException -> HttpStatus.GONE

            is DisabledException, is LockedException -> HttpStatus.LOCKED

            is MaxUploadSizeExceededException,
            is BadCredentialsException,
            is JwtException -> HttpStatus.BAD_REQUEST

            is AccessDeniedException -> HttpStatus.FORBIDDEN

            is AuthenticationException -> HttpStatus.NOT_FOUND
            else -> {
                e.printStackTrace()
                logger.error { e.stackTrace }
                HttpStatus.I_AM_A_TEAPOT
            }
        }

        logger.error { "Status: $status, message: ${e.message}" }
        return ResponseEntity(Error(Error.StatusCode(status.value(), status.name), e.message), status)
    }
}