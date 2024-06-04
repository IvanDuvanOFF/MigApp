package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 410
 */
class TfaCodeExpiredException(message: String = "Code is expired") : MigApplicationException(HttpStatus.GONE, message)