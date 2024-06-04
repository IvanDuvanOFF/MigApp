package org.example.migapi.domain.typography.dto

/**
 * Оформление с массивом типов документов, для этого оформления
 */
data class TypographyTitleDto(
    val id: String,
    val title: String,
    val status: String,
    val date: String
)
