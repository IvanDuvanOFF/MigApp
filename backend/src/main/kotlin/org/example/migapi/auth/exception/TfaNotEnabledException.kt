package org.example.migapi.auth.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

class TfaNotEnabledException :
    MigApplicationException(HttpStatus.FORBIDDEN, "Two factor authentication not allowed for this user")