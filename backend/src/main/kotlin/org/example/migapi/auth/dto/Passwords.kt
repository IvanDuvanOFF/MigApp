package org.example.migapi.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.migapi.domain.account.validation.ValidPassword
import java.io.Serializable

data class Passwords(
    @Schema(example = "password", required = true)
    @ValidPassword
    val password: String,

    @Schema(example = "password", required = true)
    @ValidPassword
    val confirmation: String
) : Serializable
