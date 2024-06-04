package org.example.migapi.domain.account.dto

import org.example.migapi.domain.account.validation.ValidEmail

/**
 * Дтошка для смены адреса электронной почты
 *
 * @property email новый адрес электронной почты [String]
 */
data class EmailDto(
    @ValidEmail
    val email: String
)
