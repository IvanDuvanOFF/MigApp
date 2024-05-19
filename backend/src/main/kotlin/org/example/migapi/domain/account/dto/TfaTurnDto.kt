package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class TfaTurnDto(
    @JsonProperty("tfa_enabled")
    @Schema(required = true)
    val tfaEnabled: Boolean
)
