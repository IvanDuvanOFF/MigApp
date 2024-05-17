package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.TotpCode
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
interface TotpCodeRepository : JpaRepository<TotpCode, TotpCode.TotpCodeId> {

    @Cacheable("tfa_user_id", key = "#user.id")
    fun findAllByTfaIdUser(user: User): Optional<List<TotpCode>>

    @Caching(evict = [CacheEvict("tfa_user_id", key = "#entity.component1().user.id", allEntries = true)])
    override fun delete(entity: TotpCode)

    @Caching(put = [CachePut("tfa_user_id", key = "#entity.component1().user.id")])
    override fun <S : TotpCode> save(entity: S): S
}