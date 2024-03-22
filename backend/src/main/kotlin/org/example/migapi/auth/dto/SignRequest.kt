package org.example.migapi.auth.dto

import io.swagger.v3.oas.annotations.media.Schema


data class SignRequest(
    @Schema(example = "aboba", required = true)
    val login: String,

    @Schema(example = "password", required = true)
    val password: String
)
