package org.example.migapi.domain.dto.auth

data class VerificationRequest(
    val username: String,
    val code: String
)