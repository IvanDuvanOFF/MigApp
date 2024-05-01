package org.example.migapi.utils

import com.google.gson.Gson
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class MigUtils {
    val logger = KotlinLogging.logger {  }

    fun getHostUrl(request: HttpServletRequest): String {
        val replace = request
            .requestURL
            .toString()
            .replace(request.servletPath, "")
        return replace
    }

    fun getRemoteAddress(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")
        logger.info { "IP 1 = $ip" }
        ip = request.getHeader("X-Real-IP")
        logger.info { "IP 2 = $ip" }
        ip = request.remoteHost
        logger.info { "IP 3 = $ip" }
        ip = request.remoteAddr
        logger.info { "IP 4 = $ip" }
        ip = request.remoteUser
        logger.info { "IP 5 = $ip" }
        logger.info { "User info = ${Gson().toJson(request)}" }

        return request
            .getHeader("X-Forwarded-For")?.let { it.split(',')[0].trim() } ?: request.remoteAddr
    }
}