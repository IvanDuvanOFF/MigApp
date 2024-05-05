package org.example.migapi.core.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Error(
    @Schema(required = true)
    val status: StatusCode,

    @Schema(example = "Email is not correct", required = false)
    val message: String? = null
) {
    data class StatusCode(
        @Schema(example = "404", required = true)
        val code: Int,

        @Schema(example = "NOT_FOUND", required = true)
        val name: String
    )
}
