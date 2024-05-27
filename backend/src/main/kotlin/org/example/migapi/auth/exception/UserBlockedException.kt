package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class UserBlockedException : MigApplicationException(HttpStatus.CONFLICT, "User already blocked")