package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class BadCredentialsException(message: String = "Username or password are wrong") :
    MigApplicationException(HttpStatus.BAD_REQUEST, message)