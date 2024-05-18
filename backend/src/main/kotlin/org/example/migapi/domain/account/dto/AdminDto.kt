package org.example.migapi.domain.account.dto

import org.example.migapi.core.domain.model.enums.ERole

class AdminDto(
    id: String? = null,
    username: String,
    email: String,
    password: String,
    isActive: Boolean = false,
    val name: String,
    val surname: String
) : UserDto(id, username, email, password, isActive, ERole.ROLE_ADMIN.name)