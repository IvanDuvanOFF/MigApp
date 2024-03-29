package org.example.migapi

import com.google.gson.Gson
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import java.time.format.DateTimeFormatter

@SpringBootApplication
@EnableCaching
class MigApiApplication {
	@Bean
	fun gson(): Gson = Gson()

	@Bean
	fun dateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
}

fun main(args: Array<String>) {
	runApplication<MigApiApplication>(*args)
}