package org.example.migapi.domain.dto.auth

data class SignRequest(
    val login: String,
    val password: String
)
