package org.example.migapi.utils

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class EmailValidator {
    private val emailPattern =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"

    private val pattern = Pattern.compile(emailPattern)

    fun validate(email: String) = pattern.matcher(email).matches()
}