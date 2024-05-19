package org.example.migapi.domain.notification.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.example.migapi.core.domain.dto.Dto
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class NotificationDto(

    @JsonProperty("notification_id", required = true)
    @Schema(required = true)
    val id: String,

    @JsonProperty("user_id", required = false)
    @Schema(required = false)
    val userId: String = "",

    @JsonProperty(required = false)
    @Schema(required = false)
    val title: String = "",

    @JsonProperty(required = false)
    @Schema(required = false)
    val description: String = "",

    @JsonProperty(required = false)
    @Schema(required = false)
    val date: Date? = null,

    @JsonProperty("is_viewed")
    @Schema(required = false)
    val isViewed: Boolean? = false,

    @JsonProperty(required = false)
    @Schema(required = false)
    val status: String = ""
) : Dto
