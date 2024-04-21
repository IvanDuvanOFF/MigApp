package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class PasswordsAreNotEqualException : MigApplicationException(HttpStatus.BAD_REQUEST, "Passwords are not equal")