package org.example.migapi.domain.files.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 400
 */
class FilenameNotFoundException(message: String = "Filename not found") : MigApplicationException(HttpStatus.BAD_REQUEST, message)