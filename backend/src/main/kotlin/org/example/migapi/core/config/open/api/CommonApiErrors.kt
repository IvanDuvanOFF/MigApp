package org.example.migapi.core.config.open.api

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.example.migapi.core.domain.dto.Error

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@ApiResponses(value = [
    ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = [Content(schema = Schema(implementation = Error::class))]
    ),
    ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = [Content(schema = Schema(implementation = Error::class))]
    ),
    ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = [Content(schema = Schema(implementation = Error::class))]
    )
])
annotation class CommonApiErrors
