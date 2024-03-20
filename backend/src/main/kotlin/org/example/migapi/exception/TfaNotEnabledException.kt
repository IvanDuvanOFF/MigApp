package org.example.migapi.exception

import org.example.migapi.exception.core.MigApplicationException
import org.springframework.http.HttpStatus

class TfaNotEnabledException :
    MigApplicationException(HttpStatus.FORBIDDEN, "Two factor authentication not allowed for this user")