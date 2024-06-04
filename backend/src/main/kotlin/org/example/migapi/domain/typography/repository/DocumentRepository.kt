package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.Document
import org.example.migapi.domain.typography.model.Typography
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Репохиторий для документов [Document]
 */
@Repository
@CacheConfig(cacheNames = ["typography-id", "document-id"])
interface DocumentRepository : JpaRepository<Document, UUID> {

    /**
     * Сохраняет документ [entity]. Удаляет офрмление [Typography] из кэша "typography-id"
     * и добавляет документ [Document] в хранилище кэша "document-id"
     */
    @Caching(
        evict = [CacheEvict("typography-id", key = "#entity.typographyId")],
        put = [CachePut("document-id", key = "#entity.id")]
    )
    override fun <S : Document> save(entity: S): S
}