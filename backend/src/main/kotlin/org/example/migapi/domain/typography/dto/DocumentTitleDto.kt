package org.example.migapi.domain.typography.dto

data class DocumentTitleDto(
    val id: String? = null,
    val title: String,
    val link: String? = null,
    val status: String? = null
)