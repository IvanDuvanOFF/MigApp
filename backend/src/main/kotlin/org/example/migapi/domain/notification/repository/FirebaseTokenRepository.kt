package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.notification.model.FirebaseToken
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@CacheConfig(cacheNames = ["firebase-token-id"])
interface FirebaseTokenRepository : JpaRepository<FirebaseToken, String> {

    @Cacheable("firebase-token-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<FirebaseToken>

    @CachePut("firebase-token-id", key = "#entity.token")
    override fun <S : FirebaseToken> save(entity: S): S

    @CacheEvict("firebase-token-id", key = "#entity.token")
    override fun delete(entity: FirebaseToken)

    @CacheEvict("firebase-token-id", allEntries = true)
    override fun deleteAll()
}