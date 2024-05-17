package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BlockRequest(
    @Schema(required = false)
    val email: String = "",

    @Schema(required = false)
    val phone: String = ""
)
