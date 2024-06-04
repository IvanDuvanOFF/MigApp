package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Дтошка для включения двухфакторной аутентификации
 *
 * @property tfaEnabled включить или выключить 2х факторную аутентификаю [Boolean]
 */
data class TfaTurnDto(
    @JsonProperty("tfa_enabled")
    @Schema(required = true)
    val tfaEnabled: Boolean
)
