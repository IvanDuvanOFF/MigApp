package org.example.migapi.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 * Запрос на вход в систему
 *
 * @property login имя пользователя или email [String]
 * @property password пароль [String]
 */
data class SignRequest(
    @Schema(example = "aboba", required = true)
    val login: String,

    @Schema(example = "password", required = true)
    val password: String
) : Serializable
