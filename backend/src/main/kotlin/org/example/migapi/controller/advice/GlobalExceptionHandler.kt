package org.example.migapi.controller.advice

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.PersistenceException
import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import org.example.migapi.domain.dto.util.Error
import org.springframework.http.converter.HttpMessageNotReadableException

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger: KLogger = KotlinLogging.logger { }

    @ExceptionHandler(
        value = [
            MigApplicationException::class,
            PersistenceException::class,
            DisabledException::class,
            BadCredentialsException::class,
            AuthenticationException::class,
            HttpMessageNotReadableException::class,
            Exception::class
        ]
    )
    fun processException(e: Exception): ResponseEntity<Error> {
        val status = when (e) {
            is MigApplicationException -> e.httpStatus
            is PersistenceException -> HttpStatus.INTERNAL_SERVER_ERROR
            is DisabledException -> HttpStatus.LOCKED
            is BadCredentialsException -> HttpStatus.FORBIDDEN
            is AuthenticationException -> HttpStatus.NOT_FOUND
            is HttpMessageNotReadableException -> HttpStatus.BAD_REQUEST
            else -> {
                logger.error { e.stackTrace }
                HttpStatus.I_AM_A_TEAPOT
            }
        }

        logger.error { "Status: $status, message: ${e.message}" }
        return ResponseEntity(Error(Error.StatusCode(status.value(), status.name), e.message), status)
    }
}