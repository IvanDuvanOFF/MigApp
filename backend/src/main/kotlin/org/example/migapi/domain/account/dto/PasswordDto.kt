package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.migapi.domain.account.validation.ValidPassword

data class PasswordDto(
    @JsonProperty("old_password")
    val oldPassword: String,

    @JsonProperty("new_password")
    @ValidPassword
    val newPassword: String
)