package org.example.migapi.domain.files.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 404
 */
class FileNotFoundException(message: String = "File not found") : MigApplicationException(HttpStatus.NOT_FOUND, message)