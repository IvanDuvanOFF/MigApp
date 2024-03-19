package org.example.migapi.domain.dto.auth

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SignResponse(
    val token: String = "",
    val refreshToken: String = "",
    val tfaEnabled: Boolean = false
)
