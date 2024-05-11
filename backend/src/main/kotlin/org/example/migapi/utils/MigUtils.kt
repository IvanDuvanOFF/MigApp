package org.example.migapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class MigUtils {

    fun getHostUrl(request: HttpServletRequest): String {
        val replace = request
            .requestURL
            .toString()
            .replace(request.servletPath, "")
        return replace
    }

    fun getRemoteAddress(request: HttpServletRequest): String {
        return request
            .getHeader("X-Forwarded-For")?.let { it.split(',')[0].trim() } ?: request.remoteAddr
    }
}