package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

/**
 * @property httpStatus 404
 */
class NotFoundException(message: String = "Not found") : MigApplicationException(HttpStatus.NOT_FOUND, message)