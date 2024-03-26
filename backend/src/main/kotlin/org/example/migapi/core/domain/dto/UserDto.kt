package org.example.migapi.core.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.annotations.Expose
import org.example.migapi.core.domain.model.enums.ERole

open class UserDto(
    val id: String? = null,
    val username: String,
    val password: String,
    val isActive: Boolean = false,
    val role: String = ERole.ROLE_USER.name,

    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    val tfaEnabled: Boolean = false
) : Dto