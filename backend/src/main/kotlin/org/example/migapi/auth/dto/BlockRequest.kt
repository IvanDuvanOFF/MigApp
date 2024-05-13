package org.example.migapi.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BlockRequest(
    val email: String = "",
    val phone: String = ""
)
