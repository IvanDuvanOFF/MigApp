package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 400
 */
class UserAlreadyExistsException(message: String) : MigApplicationException(HttpStatus.BAD_REQUEST, message)