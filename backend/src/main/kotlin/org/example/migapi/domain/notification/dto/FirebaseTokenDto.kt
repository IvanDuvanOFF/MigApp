package org.example.migapi.domain.notification.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.example.migapi.core.domain.dto.Dto

/**
 * Токен для Firebase для отправки уведомлений на устройсва пользователя
 */
data class FirebaseTokenDto(
    @Schema(required = true, example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuY2hpY2siLCJpYXQiOjE3MTYwMjk1MjMsImV4cCI6MTcxNjYzNDMyM30.3zjwY2AJvrqpE_ej3GVoORq7m0rsDgriwF70S6usZ8M")
    val token: String,

    @JsonProperty("user_id")
    @Schema(required = true, example = "a9b045a4-fb53-4cc4-bb65-013334f29296")
    val userId: String
) : Dto
