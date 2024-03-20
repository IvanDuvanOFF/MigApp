package org.example.migapi.core.domain.dto

data class Error(
    val status: StatusCode,
    val message: String?
) {
    data class StatusCode(
        val code: Int,
        val name: String
    )
}
