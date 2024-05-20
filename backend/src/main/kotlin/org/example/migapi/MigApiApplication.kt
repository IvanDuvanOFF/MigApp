package org.example.migapi

import com.google.gson.Gson
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.domain.model.SpringUser
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@SpringBootApplication
@EnableCaching
class MigApiApplication {
    @Bean
    fun gson(): Gson = Gson()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun corsGlobalConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("*")
            }
        }
    }
}

fun Boolean.orThrow(throwable: () -> Throwable) {
    if (!this) throw throwable()
}

fun String.toUUID(): UUID = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    throw BadRequestException()
}

fun LocalDate.toDate(): Date = Date.from(
    this.atStartOfDay().atZone(ZoneId.systemDefault())
        .toInstant()
)

fun getUsernameFromContext(): String =
    (SecurityContextHolder.getContext().authentication.principal as SpringUser).username

fun main(args: Array<String>) {
    runApplication<MigApiApplication>(*args)
}