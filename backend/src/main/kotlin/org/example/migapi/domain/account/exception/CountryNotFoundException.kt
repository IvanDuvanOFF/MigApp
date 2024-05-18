package org.example.migapi.domain.account.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class CountryNotFoundException(message: String) : MigApplicationException(HttpStatus.NOT_FOUND, message)