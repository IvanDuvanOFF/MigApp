package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Запрос на восстановление доступа к аккаунту или по email или по номеру телефона
 *
 * @property email email [String]
 * @property phone номер телефона [String]
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BlockRequest(
    @Schema(required = false)
    val email: String = "",

    @Schema(required = false)
    val phone: String = ""
)
