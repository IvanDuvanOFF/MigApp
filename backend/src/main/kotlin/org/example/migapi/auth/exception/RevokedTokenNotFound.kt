package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class RevokedTokenNotFound(message: String = "Token not found") :
    MigApplicationException(HttpStatus.BAD_REQUEST, message)