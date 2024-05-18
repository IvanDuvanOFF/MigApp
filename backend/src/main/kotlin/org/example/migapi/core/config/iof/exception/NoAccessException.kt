package org.example.migapi.core.config.iof.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class NoAccessException : MigApplicationException(HttpStatus.FORBIDDEN, "You have no access to this resource")