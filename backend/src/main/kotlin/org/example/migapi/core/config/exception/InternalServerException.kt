package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

class InternalServerException(message: String = "Internal error") :
    MigApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, message)