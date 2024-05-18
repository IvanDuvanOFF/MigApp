package org.example.migapi.core.config.iof.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class FileNotFoundException(message: String = "File not found") : MigApplicationException(HttpStatus.NOT_FOUND, message)