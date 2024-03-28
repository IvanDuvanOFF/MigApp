package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class VerificationTokenNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)