package org.example.migapi.domain.typography.dto

/**
 * Тип офрмления
 */
data class TypographyDto(
    val id: String,
    val title: String,
    val status: String,
    val date: String,
    val documents: List<DocumentDto>
)
