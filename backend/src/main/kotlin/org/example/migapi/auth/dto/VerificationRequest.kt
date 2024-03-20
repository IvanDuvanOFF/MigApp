package org.example.migapi.auth.dto

data class VerificationRequest(
    val username: String,
    val code: String
)