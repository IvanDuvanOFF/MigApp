package org.example.migapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class MigUtils {
    fun getHostUrl(request: HttpServletRequest): String = request
        .requestURL
        .toString()
        .replace(request.servletPath, "")
}