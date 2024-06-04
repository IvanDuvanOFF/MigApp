package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 409
 */
class UserAlreadyActivatedException(message: String = "User is active") :
    MigApplicationException(HttpStatus.CONFLICT, message)