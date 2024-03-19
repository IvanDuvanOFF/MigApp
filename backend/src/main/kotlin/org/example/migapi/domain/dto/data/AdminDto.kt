package org.example.migapi.domain.dto.data

class AdminDto(
    id: String? = null,
    username: String,
    password: String,
    isActive: Boolean = false,
    val name: String,
    val surname: String
) : UserDto(id, username, password, isActive, "ROLE_ADMIN")