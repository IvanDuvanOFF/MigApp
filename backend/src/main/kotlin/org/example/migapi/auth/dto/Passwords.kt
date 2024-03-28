package org.example.migapi.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class Passwords(
    @Schema(example = "password", required = true)
    val password: String,

    @Schema(example = "password", required = true)
    val confirmation: String
) : Serializable
