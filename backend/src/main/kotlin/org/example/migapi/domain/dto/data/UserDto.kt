package org.example.migapi.domain.dto.data

import org.example.migapi.domain.dto.Dto
import org.example.migapi.domain.model.enums.ERole

open class UserDto(
    val id: String? = null,
    val username: String,
    val password: String,
    val isActive: Boolean = false,
    val role: String = ERole.ROLE_USER.name
) : Dto