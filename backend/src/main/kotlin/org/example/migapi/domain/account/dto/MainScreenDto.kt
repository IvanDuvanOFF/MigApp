package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Данные, необходимые для главного экрана приложения
 *
 * @property userId id пользователя [String]
 * @property logo ссылка на логотип приложения [String]
 * @property name имя пользователя [String]
 * @property surname фамилия пользователя [String]
 * @property patronymic отчество пользователя [String]
 * @property photo имя файла аватарки пользователя [String]
 * @property institute институт пользователя [String]
 * @property group группа пользователя [String]
 */
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
