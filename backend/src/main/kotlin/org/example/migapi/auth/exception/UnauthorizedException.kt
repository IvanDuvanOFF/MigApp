package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class UnauthorizedException : MigApplicationException(HttpStatus.UNAUTHORIZED, "You are not signed in")