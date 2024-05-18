package org.example.migapi.core.config.iof.repository

import org.example.migapi.core.config.iof.model.File
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["file-id"])
interface FileRepository : JpaRepository<File, String> {

    @Cacheable("file-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<File>

    @CachePut("file-id", key = "#entity.name")
    override fun <S : File> save(entity: S): S

    @CacheEvict("file-id", key = "#entity.name")
    override fun delete(entity: File)
}