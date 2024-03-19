package org.example.migapi.exception.core

import org.springframework.http.HttpStatus

sealed interface MigException {
    val httpStatus: HttpStatus
    val message: String
}