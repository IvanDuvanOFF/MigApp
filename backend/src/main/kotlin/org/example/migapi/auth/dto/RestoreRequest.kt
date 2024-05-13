package org.example.migapi.auth.dto

data class RestoreRequest(
    val verification: VerificationRequest,
    val  passwords: Passwords
)
