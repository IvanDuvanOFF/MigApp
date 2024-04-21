package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class UserAlreadyExistsException(message: String) : MigApplicationException(HttpStatus.BAD_REQUEST, message)