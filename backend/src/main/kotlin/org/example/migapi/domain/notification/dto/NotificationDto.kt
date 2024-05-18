package org.example.migapi.domain.notification.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.example.migapi.core.domain.dto.Dto
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class NotificationDto(

    @Schema(required = true)
    val id: String,

    @JsonProperty("user_id")
    @Schema(required = true)
    val userId: String,

    @Schema(required = false)
    val title: String,

    @Schema(required = false)
    val description: String,

    @Schema(required = false)
    val date: Date,

    @JsonProperty("is_viewed")
    @Schema(required = true)
    val isViewed: Boolean,

    @Schema(required = true)
    val status: String
) : Dto
