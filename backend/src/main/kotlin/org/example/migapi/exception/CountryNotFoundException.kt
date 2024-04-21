package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class CountryNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)