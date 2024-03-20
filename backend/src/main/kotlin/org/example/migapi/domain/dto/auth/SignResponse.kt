package org.example.migapi.domain.dto.auth

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SignResponse(
    @JsonProperty("access_token")
    val token: String = "",

    @JsonProperty("refresh_token")
    val refreshToken: String = "",

    @JsonProperty("tfa_enabled")
    val tfaEnabled: Boolean = false
)
