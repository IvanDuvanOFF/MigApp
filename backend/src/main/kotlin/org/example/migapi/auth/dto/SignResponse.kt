package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SignResponse(
    @JsonProperty("access_token")
    @Schema(required = false)
    val token: String = "",

    @JsonProperty("refresh_token")
    @Schema(required = false)
    val refreshToken: String = "",

    @JsonProperty("tfa_enabled")
    val tfaEnabled: Boolean = false
)
