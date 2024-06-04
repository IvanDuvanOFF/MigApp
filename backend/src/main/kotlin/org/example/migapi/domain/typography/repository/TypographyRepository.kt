package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.Typography
import org.springframework.cache.annotation.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для оформлений [Typography]
 */
@Repository
@CacheConfig(cacheNames = ["typography-user-name", "typography-id"])
interface TypographyRepository : JpaRepository<Typography, UUID> {

    /**
     * Находит все офрмления [List]<[Typography]> пользователя по его имени [username].
     * Кэширует не пустые результаты в хранилице "typography-user-name"
     */
    @Cacheable("typography-user-name", key = "#username", unless = "#result.isEmpty()")
    fun findAllByUserUsername(username: String): List<Typography>

    /**
     * Находит оформление [Typography] по его id [id].
     * Результат кэширует в хранилище "typography-id"
     */
    @Cacheable("typography-id", key = "#id")
    override fun findById(id: UUID): Optional<Typography>

    /**
     * Удаляет оформление [entity].
     * Очищает польностью хранилище кэша "typography-user-name".
     * Удаляет данное офрмление из хранилища "typography-id"
     */
    @Caching(
        evict = [
            CacheEvict("typography-user-name", allEntries = true),
            CacheEvict("typography-id", key = "#entity.id")
        ]
    )
    override fun delete(entity: Typography)

    /**
     * Сохраняет оформление [entity]. Так же добавляет его в кэш "typography-id"
     * и очищает хранилище "typography-user-name"
     */
    @Caching(
        put = [CachePut("typography-id", key = "#entity.id")],
        evict = [CacheEvict("typography-user-name", allEntries = true)]
    )
    override fun <S : Typography> save(entity: S): S
}