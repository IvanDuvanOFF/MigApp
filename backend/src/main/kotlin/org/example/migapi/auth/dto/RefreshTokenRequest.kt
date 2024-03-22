package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class RefreshTokenRequest(
    @JsonProperty("refresh_token")
    @Schema(required = true)
    val refreshToken: String
)
