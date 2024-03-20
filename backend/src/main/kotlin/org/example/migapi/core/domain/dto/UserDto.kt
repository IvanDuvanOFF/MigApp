package org.example.migapi.core.domain.dto

import org.example.migapi.core.domain.model.enums.ERole

open class UserDto(
    val id: String? = null,
    val username: String,
    val password: String,
    val isActive: Boolean = false,
    val role: String = ERole.ROLE_USER.name
) : Dto