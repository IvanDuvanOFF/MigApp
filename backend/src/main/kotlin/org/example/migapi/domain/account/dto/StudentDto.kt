package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

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
    val sex: String,
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