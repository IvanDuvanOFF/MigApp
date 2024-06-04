package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 * Ответ на запрос на вход в систему
 *
 * @property token jwt-токен для доступа в систему [String]
 * @property refreshToken токен для обновления [token] [String]
 * @property username имя пользователя [String]
 * @property tfaEnabled флаг включена ли 2х факторная аутентификация [Boolean]
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SignResponse(
    @JsonProperty("access_token")
    @SerializedName("access_token")
    @Schema(required = false)
    val token: String = "",

    @JsonProperty("refresh_token")
    @SerializedName("refresh_token")
    @Schema(required = false)
    val refreshToken: String = "",

    @JsonProperty("username")
    @SerializedName("username")
    @Schema(required = false)
    val username: String = "",

    @JsonProperty("tfa_enabled")
    @SerializedName("tfa_enabled")
    @Schema(required = false)
    val tfaEnabled: Boolean? = null
) : Serializable
