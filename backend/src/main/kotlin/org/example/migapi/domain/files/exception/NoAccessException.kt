package org.example.migapi.domain.files.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 403
 */
class NoAccessException : MigApplicationException(HttpStatus.FORBIDDEN, "You have no access to this resource")