package org.example.migapi.domain.files.repository

import org.example.migapi.domain.files.model.File
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для работы с файлами пользователей
 */
@Repository
@CacheConfig(cacheNames = ["file-id"])
interface FileRepository : JpaRepository<File, String> {

    /**
     * Находит файл [File] по его id [id].
     * Кэширует результат в хранилище "file-id"
     */
    @Cacheable("file-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<File>

    /**
     * Сохраняет файл [entity] в систему.
     * Добавляет [entity] в кэш "file-id"
     */
    @CachePut("file-id", key = "#entity.name")
    override fun <S : File> save(entity: S): S

    /**
     * Удаляет файл [entity] из системы.
     * Удаляет [entity] из кэша "file-id"
     */
    @CacheEvict("file-id", key = "#entity.name")
    override fun delete(entity: File)
}