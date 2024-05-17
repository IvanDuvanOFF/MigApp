package org.example.migapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.exception.UnauthorizedException
import org.springframework.cache.annotation.Cacheable
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

    @Cacheable(key = "#string", cacheNames = ["is_email"], condition = "#string.length() > 0")
    fun isEmail(string: String): Boolean {
        return "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
            .toRegex(RegexOption.IGNORE_CASE)
            .matches(string)
    }

    @Cacheable(key = "#string", cacheNames = ["is_phone"], condition = "#string.length() > 0")
    fun isPhone(string: String): Boolean {
        return "^(?:\\+?[0-9]{1,3})?[-.\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?[0-9]{4}\$"
            .toRegex()
            .matches(string)
    }

    @Throws(exceptionClasses = [UnauthorizedException::class])
    fun extractJwt(request: HttpServletRequest): String =
        request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)
            ?: throw UnauthorizedException()
}