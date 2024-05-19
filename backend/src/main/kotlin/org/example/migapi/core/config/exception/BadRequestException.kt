package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

class BadRequestException(message: String = "Bad request") : MigApplicationException(HttpStatus.BAD_REQUEST, message)