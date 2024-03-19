package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class UserAlreadyActivatedException(message: String) : MigApplicationException(HttpStatus.CONFLICT, message)