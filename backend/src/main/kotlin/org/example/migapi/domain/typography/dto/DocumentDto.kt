package org.example.migapi.domain.typography.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class DocumentDto(
    val id: String? = null,
    val title: String,
    val status: String? = null,

    @JsonProperty("creation_date")
    val creationDate: Date? = null,

    @JsonProperty("expiration_date")
    val expirationDate: Date? = null,

    @JsonProperty("file_name")
    val fileName: String? = null
)
