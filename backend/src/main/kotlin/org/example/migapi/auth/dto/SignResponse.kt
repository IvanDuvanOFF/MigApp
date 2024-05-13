package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

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
    val username: String = "",

    @JsonProperty("tfa_enabled")
    @SerializedName("tfa_enabled")
    val tfaEnabled: Boolean = false
) : Serializable
