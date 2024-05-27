package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MainScreenDto(

    @JsonProperty("user_id")
    val userId: String,
    val logo: String,
    val name: String,
    val surname: String,

    @Schema(required = false)
    val patronymic: String? = null,

    @Schema(required = false)
    val photo: String? = null,

    @Schema(required = false)
    val institute: String? = null,

    @Schema(required = false)
    val group: String? = null
)
