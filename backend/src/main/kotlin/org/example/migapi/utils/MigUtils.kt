package org.example.migapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class MigUtils {
    fun getHostUrl(request: HttpServletRequest): String = request
            .requestURL
            .toString()
            .replace(request.servletPath, "")

    fun getRemoteAddress(request: HttpServletRequest): String = request
            .getHeader("X-Forwarded-For")?.let { it.split(',')[0].trim() } ?: request.remoteAddr
}