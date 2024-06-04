package org.example.migapi.auth.repository

import org.example.migapi.auth.model.RevokedToken
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для работы с отозванными токенами [RevokedToken]
 */
@Repository
@CacheConfig(cacheNames = ["revoked-token"])
interface RevokedTokenRepository : JpaRepository<RevokedToken, String> {

    /**
     * Находит отозванный токен [RevokedToken] по [token].
     * Сохраняет результат в кэш "revoked-token"
     */
    @Cacheable("revoked-token", key = "#token", unless = "#result == null")
    override fun findById(token: String): Optional<RevokedToken>

    /**
     * Удаляет отозванный токен [entity].
     * Удаляет [entity] из кэша "revoked-token"
     */
    @CacheEvict("revoked-token", key = "#entity.token")
    override fun delete(entity: RevokedToken)

    /**
     * Сохраняет отозванный токен [entity].
     * Сохраняет результат в кэш "revoked-token"
     */
    @CachePut("revoked-token", key = "#entity.token")
    override fun <S : RevokedToken> save(entity: S): S
}