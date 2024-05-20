package org.example.migapi.domain.typography.dto

import java.util.Date

data class TypographyDto(
    val id: String,
    val title: String,
    val status: String,
    val date: String,
    val documents: List<DocumentDto>
)
