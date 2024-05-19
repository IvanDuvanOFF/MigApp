package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.notification.model.FirebaseToken
import org.springframework.cache.annotation.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@CacheConfig(cacheNames = ["firebase-token-id"])
interface FirebaseTokenRepository : JpaRepository<FirebaseToken, String> {

    @Cacheable("firebase-token-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<FirebaseToken>

    @Caching(
        put = [
            CachePut("firebase-token-id", key = "#entity.token"),
        ]
    )
    override fun <S : FirebaseToken> save(entity: S): S

    @Caching(
        evict = [
            CacheEvict("firebase-token-id", key = "#entity.token"),
        ]
    )
    override fun delete(entity: FirebaseToken)

    @Caching(
        evict = [
            CacheEvict("firebase-token-id", allEntries = true),
        ]
    )
    override fun deleteAll()
}