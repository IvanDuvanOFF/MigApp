package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class UserAlreadyExistsException(message: String) : MigApplicationException(HttpStatus.BAD_REQUEST, message)