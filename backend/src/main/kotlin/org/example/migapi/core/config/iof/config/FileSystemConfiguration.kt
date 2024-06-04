package org.example.migapi.core.config.iof.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.example.migapi.domain.files.exception.FileNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Конфиг для файловой системы
 *
 * @property path путь до файлового хранилища
 * @property publicPath путь до общего хранилища
 */
@Configuration
class FileSystemConfiguration(
    @Value("\${storage}")
    private val storagePath: String
) {
    private val logger = KotlinLogging.logger {  }

    lateinit var path: Path
    lateinit var publicPath: Path

    /**
     * Инициализирует свойства [path] и [publicPath]
     *
     * @throws FileNotFoundException некорректный путь до хранилища
     */
    @PostConstruct
    fun initPath() {
        path = Paths.get(storagePath)
        publicPath = Paths.get("$storagePath/public")

        try {
            Files.createDirectories(path)
        } catch (e: IOException) {
            logger.error { e.message }
            logger.error { e.stackTrace }

            throw FileNotFoundException("Could not init filesystem")
        }
    }
}