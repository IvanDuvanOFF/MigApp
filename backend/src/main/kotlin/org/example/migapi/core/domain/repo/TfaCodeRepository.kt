package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.TfaCode
import org.example.migapi.core.domain.model.entity.User
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["tfa_user_id"])
interface TfaCodeRepository : JpaRepository<TfaCode, TfaCode.TfaCodeId> {

    @Cacheable("tfa_user_id", key = "#user.id")
    fun findTfaCodesByTfaIdUser(user: User): List<TfaCode>

    @Caching(evict = [CacheEvict("tfa_user_id", key = "#entity.component1().user.id", allEntries = true)])
    override fun delete(entity: TfaCode)

    @Caching(put = [CachePut("tfa_user_id", key = "#entity.component1().user.id")])
    override fun <S : TfaCode> save(entity: S): S
}