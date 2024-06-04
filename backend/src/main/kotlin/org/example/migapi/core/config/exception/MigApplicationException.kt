package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

/**
 * Родительский класс для кастомных исключений
 */
open class MigApplicationException(override val httpStatus: HttpStatus, override val message: String) : MigException,
    Exception(message)