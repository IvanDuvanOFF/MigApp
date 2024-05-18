package org.example.migapi.core.config.iof.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.migapi.core.domain.dto.Dto

data class FileUploaded(
    @Schema(required = true)
    val name: String,

    @Schema(required = true)
    val link: String
) : Dto
