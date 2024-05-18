package org.example.migapi.core.config.iof.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.example.migapi.core.config.iof.exception.FileNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
class FileSystemConfiguration(
    @Value("storage")
    private val storagePath: String
) {
    private val logger = KotlinLogging.logger {  }

    lateinit var path: Path

    @PostConstruct
    fun initPath() {
        path = Paths.get(storagePath)

        try {
            Files.createDirectories(path)
        } catch (e: IOException) {
            logger.error { e.message }
            logger.error { e.stackTrace }

            throw FileNotFoundException("Could not init filesystem")
        }
    }
}