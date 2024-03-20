package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshTokenRequest(
    @JsonProperty("refresh_token")
    val refreshToken: String
)
