package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class RoleNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)