package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

class NotFoundException(message: String = "Not found") : MigApplicationException(HttpStatus.BAD_REQUEST, message)