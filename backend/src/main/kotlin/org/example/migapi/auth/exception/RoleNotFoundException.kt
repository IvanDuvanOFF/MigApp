package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class RoleNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)