package org.example.migapi.domain.typography.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Документ
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class DocumentDto(
    val id: String? = null,
    val title: String,
    val status: String? = null,

    @JsonProperty("creation_date")
    val creationDate: String? = null,

    @JsonProperty("expiration_date")
    val expirationDate: String? = null,

    @JsonProperty("file_name")
    val fileName: String? = null
)
