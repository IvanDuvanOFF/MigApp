package org.example.migapi.domain.account.dto

import org.example.migapi.domain.account.validation.ValidEmail

data class EmailDto(
    @ValidEmail
    val email: String
)
