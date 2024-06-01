package org.example.migapi.domain.account.dto

import org.example.migapi.domain.account.validation.ValidPhone

data class PhoneDto(
    @ValidPhone
    val phone: String
)
