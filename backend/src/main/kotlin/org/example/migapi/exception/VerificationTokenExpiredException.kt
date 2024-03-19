package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class VerificationTokenExpiredException(message: String) : MigApplicationException(HttpStatus.GONE, message)