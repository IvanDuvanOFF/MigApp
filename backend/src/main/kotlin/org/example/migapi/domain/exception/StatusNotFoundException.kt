package org.example.migapi.domain.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class StatusNotFoundException(message: String = "Status not found") :
    MigApplicationException(HttpStatus.BAD_REQUEST, message)