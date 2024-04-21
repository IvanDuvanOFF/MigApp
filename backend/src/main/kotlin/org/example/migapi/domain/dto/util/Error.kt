package org.example.migapi.domain.dto.util

data class Error(
    val status: StatusCode,
    val message: String?
) {
    data class StatusCode(
        val code: Int,
        val name: String
    )
}
