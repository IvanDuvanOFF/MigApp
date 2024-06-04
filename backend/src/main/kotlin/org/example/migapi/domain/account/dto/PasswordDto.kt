package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.migapi.domain.account.validation.ValidPassword

/**
 * Дтошка для смены пароля
 *
 * @property oldPassword старый пароль [String]
 * @property newPassword новый пароль [String]
 */
data class PasswordDto(
    @JsonProperty("old_password")
    val oldPassword: String,

    @JsonProperty("new_password")
    @ValidPassword
    val newPassword: String
)