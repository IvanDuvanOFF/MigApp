package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.notification.model.FirebaseToken
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

@CacheConfig(cacheNames = ["firebase-token-id", "firebase-token-user-id"])
interface FirebaseTokenRepository : JpaRepository<FirebaseToken, String> {

    @Cacheable("firebase-token-user-id", key = "#userId", unless = "#result == null")
    @Query("select f from FirebaseToken f where f.user.id = ?1")
    fun findByUserId(userId: UUID): Optional<FirebaseToken>

    @Cacheable("firebase-token-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<FirebaseToken>

    @Caching(
        put = [
            CachePut("firebase-token-id", key = "#entity.token"),
            CachePut("firebase-token-user-id", key = "#entity.user.id")
        ]
    )
    override fun <S : FirebaseToken> save(entity: S): S

    @Caching(
        evict = [
            CacheEvict("firebase-token-id", key = "#entity.token"),
            CacheEvict("firebase-token-user-id", key = "#entity.user.id")
        ]
    )
    override fun delete(entity: FirebaseToken)

    @Caching(
        evict = [
            CacheEvict("firebase-token-id", allEntries = true),
            CacheEvict("firebase-token-user-id", allEntries = true)
        ]
    )
    override fun deleteAll()
}