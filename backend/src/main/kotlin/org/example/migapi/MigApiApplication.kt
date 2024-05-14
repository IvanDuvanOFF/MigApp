package org.example.migapi

import com.google.gson.Gson
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter


@SpringBootApplication
@EnableCaching
class MigApiApplication {
	@Bean
	fun gson(): Gson = Gson()

	@Bean
	fun dateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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

fun main(args: Array<String>) {
	runApplication<MigApiApplication>(*args)
}