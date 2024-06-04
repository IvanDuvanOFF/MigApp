package org.example.migapi.auth.dto

/**
 * Запрос на восстановление доступа
 */
data class RestoreRequest(
    val verification: VerificationRequest,
    val passwords: Passwords
)
