package org.example.migapi.domain.account.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 404
 */
class StatusNotFoundException(message: String = "Status not found") :
    MigApplicationException(HttpStatus.BAD_REQUEST, message)