package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import org.example.migapi.core.domain.dto.Dto
import org.example.migapi.core.domain.model.enums.ERole

/**
 * Дтошка пользователя
 *
 * @property id id пользователя [String]
 * @property username username пользователя [String]
 * @property email адрес электронной почты пользователя [String]
 * @property password пароль пользователя [String]
 * @property role роль пользователя [String]
 * @property tfaEnabled статус активности 2х факторной аутентификации для пользователя [Boolean]
 */
open class UserDto(
    val id: String? = null,
    val username: String,
    val email: String,
    val password: String,

    @JsonProperty("is_active")
    val isActive: Boolean = false,
    val role: String = ERole.ROLE_USER.name,

    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    val tfaEnabled: Boolean = false
) : Dto