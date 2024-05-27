package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class TfaCodeExpiredException(message: String = "Code is expired") : MigApplicationException(HttpStatus.GONE, message)