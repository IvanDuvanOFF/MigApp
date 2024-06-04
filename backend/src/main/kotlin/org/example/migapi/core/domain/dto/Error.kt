package org.example.migapi.core.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Объект сообщения об ошибке
 *
 * @property status статус код [StatusCode]
 * @property message сообщение [String]
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Error(
    @Schema(required = true)
    val status: StatusCode,

    @Schema(example = "Email is not correct", required = false)
    val message: String? = null
) {

    /**
     * Класс статус-кода
     *
     * @property code код ошибки [Int]
     * @property name имя кода ошибки [String]
     */
    data class StatusCode(
        @Schema(example = "404", required = true)
        val code: Int,

        @Schema(example = "NOT_FOUND", required = true)
        val name: String
    )
}
