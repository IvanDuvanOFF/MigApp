package org.example.migapi.domain.account.dto

import org.example.migapi.domain.account.validation.ValidPhone

/**
 * Дтошка для смены номера телефона
 *
 * @property phone старый пароль [String]
 */
data class PhoneDto(
    @ValidPhone
    val phone: String
)
