package org.example.migapi.core.domain.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class CountryNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)