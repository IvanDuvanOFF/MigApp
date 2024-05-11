package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class RefreshTokenExpiredException(message: String = "Refresh token is expired") :
    MigApplicationException(HttpStatus.GONE, message)