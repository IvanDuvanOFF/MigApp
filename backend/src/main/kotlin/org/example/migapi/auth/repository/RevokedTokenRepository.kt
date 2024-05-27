package org.example.migapi.auth.repository

import org.example.migapi.auth.model.RevokedToken
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["revoked-token"])
interface RevokedTokenRepository : JpaRepository<RevokedToken, String> {

    @Cacheable("revoked-token", key = "#token", unless = "#result == null")
    override fun findById(token: String): Optional<RevokedToken>

    @CacheEvict("revoked-token", key = "#entity.token")
    override fun delete(entity: RevokedToken)

    @CachePut("revoked-token", key = "#entity.token")
    override fun <S : RevokedToken> save(entity: S): S
}