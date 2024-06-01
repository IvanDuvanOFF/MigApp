package org.example.migapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.exception.UnauthorizedException
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.exception.InternalServerException
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Component
@Scope("prototype")
class MigUtils {

    companion object {
        const val EMAIL_PATTERN =
            "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"

        const val PHONE_PATTERN =
            "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$"

        const val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$"

        private const val DATE_CONVERSION_ERROR = "Date has incorrect format"
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")

    fun localDateToString(localDate: LocalDate): String = try {
        localDate.format(dateFormatter)
    } catch (e: DateTimeException) {
        throw InternalServerException(DATE_CONVERSION_ERROR)
    }

    fun stringToLocalDate(string: String): LocalDate = try {
        LocalDate.parse(string, dateFormatter)
    } catch (e: DateTimeParseException) {
        throw BadRequestException(DATE_CONVERSION_ERROR)
    }

    fun localDateTimeToString(localDateTime: LocalDateTime): String = try {
        localDateTime.format(dateTimeFormatter)
    } catch (e: DateTimeException) {
        throw InternalServerException(DATE_CONVERSION_ERROR)
    }

    fun stringToLocalDateTime(string: String): LocalDateTime = try {
        LocalDateTime.parse(string, dateTimeFormatter)
    } catch (e: DateTimeParseException) {
        throw BadRequestException(DATE_CONVERSION_ERROR)
    }

    @Cacheable(key = "#string", cacheNames = ["is_email"], condition = "#string.length() > 0")
    fun validateEmail(string: String): Boolean = EMAIL_PATTERN.toRegex(RegexOption.IGNORE_CASE).matches(string)

    @Cacheable(key = "#string", cacheNames = ["is_phone"], condition = "#string.length() > 0")
    fun validatePhone(string: String): Boolean = PHONE_PATTERN.toRegex().matches(string)

    fun validatePassword(password: String): Boolean = PASSWORD_PATTERN.toRegex().matches(password)

    @Throws(exceptionClasses = [UnauthorizedException::class])
    fun extractJwt(request: HttpServletRequest): String =
        request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)
            ?: throw UnauthorizedException()
}