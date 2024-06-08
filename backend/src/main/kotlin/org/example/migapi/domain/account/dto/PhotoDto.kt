package org.example.migapi.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PhotoDto(
    @JsonProperty("file_name")
    val fileName: String,
)
