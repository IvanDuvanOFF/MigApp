package org.example.migapi.core.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.example.migapi.auth.exception.BadCredentialsException

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class StudentDto(
    id: String? = null,
    username: String,
    email: String,
    password: String? = null,
    isActive: Boolean = false,
    val name: String,
    val surname: String,
    val patronymic: String? = null,
    val phone: String,
    val country: String,
    val birthday: String,
    val status: String
) : UserDto(
    id,
    username,
    email,
    password ?: throw BadCredentialsException("Password cannot be null"),
    isActive,
    "ROLE_USER"
)