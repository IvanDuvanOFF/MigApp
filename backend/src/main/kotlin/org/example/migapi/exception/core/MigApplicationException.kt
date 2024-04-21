package org.example.migapi.exception.core

import org.springframework.http.HttpStatus

open class MigApplicationException(override val httpStatus: HttpStatus, override val message: String) : MigException,
    Exception(message)