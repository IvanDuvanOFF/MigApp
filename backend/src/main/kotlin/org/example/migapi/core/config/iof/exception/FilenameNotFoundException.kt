package org.example.migapi.core.config.iof.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class FilenameNotFoundException(message: String = "Filename not found") : MigApplicationException(HttpStatus.BAD_REQUEST, message)