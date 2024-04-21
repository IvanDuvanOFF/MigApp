package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class TokenExpiredException(message: String) : MigApplicationException(HttpStatus.GONE, message)