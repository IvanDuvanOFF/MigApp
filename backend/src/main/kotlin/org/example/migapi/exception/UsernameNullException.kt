package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class UsernameNullException(message: String) : MigApplicationException(HttpStatus.BAD_REQUEST, message)