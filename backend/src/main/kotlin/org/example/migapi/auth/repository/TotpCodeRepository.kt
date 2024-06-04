package org.example.migapi.auth.repository

import org.example.migapi.auth.model.TotpCode
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиотрий для работы с одноразовыми паролями [TotpCode]
 */
@Repository
@CacheConfig(cacheNames = ["tfa_user_id"])
interface TotpCodeRepository : JpaRepository<TotpCode, TotpCode.TotpCodeId> {

    /**
     * Находит [TotpCode] по [userId] пользователя.
     * Сохраняет результат в кэш "tfa_user_id"
     */
    @Cacheable("tfa_user_id", key = "#userId", unless = "#result == null")
    @Query("select t from TotpCode t where t.tfaId.user.id = ?1 order by t.expirationDate desc limit 1")
    fun findByTfaIdUser(userId: UUID): Optional<TotpCode>

    /**
     * Удаляет [entity].
     * Удаляет сущность из кэша "tfa_user_id"
     */
    @Caching(evict = [CacheEvict("tfa_user_id", key = "#entity.component1().user.id", allEntries = true)])
    override fun delete(entity: TotpCode)

    /**
     * Сохраняет сущность [entity].
     * Сохраняет результат в кэш "tfa_user_id"
     */
    @Caching(put = [CachePut("tfa_user_id", key = "#entity.component1().user.id")])
    override fun <S : TotpCode> save(entity: S): S
}