package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Дтошка студента
 *
 * @property id id пользователя [String]
 * @property username username пользователя [String]
 * @property email адрес электронной почты пользователя [String]
 * @property password пароль пользователя [String]
 * @property isActive статус активности пользователя [Boolean]
 * @property name имя пользователя [String]
 * @property surname фамилия пользователя [String]
 * @property patronymic отчество пользователя [String]
 * @property photo имя файла аватарки пользователя [String]
 * @property institute институт пользователя [String]
 * @property group группа пользователя [String]
 * @property sex пол пользователя [String]
 * @property phone номер телефона пользователя [String]
 * @property country страна пользователя [String]
 * @property birthday день рождения пользователя [String]
 * @property status статус пользователя [String]
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class StudentDto(
    id: String? = null,
    username: String,
    email: String,
    password: String = "",
    
    @JsonProperty("is_active")
    isActive: Boolean = false,

    val name: String,
    val surname: String,
    val patronymic: String? = null,
    val institute: String? = null,
    val group: String? = null,
    val photo: String? = null,
    val sex: String,

    @JsonProperty("phone_number")
    val phone: String,
    val country: String,
    val birthday: String,
    val status: String
) : UserDto(
    id,
    username,
    email,
    password,
    isActive,
    "ROLE_USER"
)