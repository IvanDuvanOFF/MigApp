package org.example.migapi.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 * Запрос на верификацию одноразового пароля
 */
data class VerificationRequest(
    @Schema(example = "user", required = true)
    val username: String,

    @Schema(example = "e2o41p", required = true)
    val code: String
) : Serializable