package org.example.migapi.core.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

data class Error(
    @Schema(required = true)
    val status: StatusCode,

    @Schema(example = "Email is not correct", required = false)
    val message: String?
) {
    data class StatusCode(
        @Schema(example = "404", required = true)
        val code: Int,

        @Schema(example = "NOT_FOUND", required = true)
        val name: String
    )
}
