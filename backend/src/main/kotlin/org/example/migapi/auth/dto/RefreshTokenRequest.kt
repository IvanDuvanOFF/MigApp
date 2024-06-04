package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 * Запрос на обновление токена доступа
 */
data class RefreshTokenRequest(
    @JsonProperty("refresh_token")
    @Schema(required = true)
    val refreshToken: String
) : Serializable
