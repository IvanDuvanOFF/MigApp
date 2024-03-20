package org.example.migapi.core.config.exception

import org.springframework.http.HttpStatus

sealed interface MigException {
    val httpStatus: HttpStatus
    val message: String
}