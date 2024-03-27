package org.example.migapi.core.domain.dto

class AdminDto(
    id: String? = null,
    username: String,
    email: String,
    password: String,
    isActive: Boolean = false,
    val name: String,
    val surname: String
) : UserDto(id, username, email, password, isActive, "ROLE_ADMIN")