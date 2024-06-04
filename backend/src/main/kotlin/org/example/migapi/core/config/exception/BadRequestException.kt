package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

/**
 * @property httpStatus 400
 */
class BadRequestException(message: String = "Bad request") : MigApplicationException(HttpStatus.BAD_REQUEST, message)