package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.notification.model.FirebaseToken
import org.springframework.cache.annotation.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Репозиторий для токенов Firebase
 */
@CacheConfig(cacheNames = ["firebase-token-id", "firebase-token-user-name"])
interface FirebaseTokenRepository : JpaRepository<FirebaseToken, String> {

    /**
     * Находит токен Firebase по его id [id].
     * Кэширует результат в хранилище "firebase-token-id"
     */
    @Cacheable("firebase-token-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<FirebaseToken>

    /**
     * Находит все токены Firebase [List]<[FirebaseToken]> по имени пользователя [user].
     * Кэширует результат в хранилище "firebase-token-user-name"
     */
    @Cacheable("firebase-token-user-name", key = "#user.username", unless = "#result.isEmpty()")
    fun findAllByUser(user: User): List<FirebaseToken>

    /**
     * Сохраняет токен Firebase [entity] в системе.
     * Кэширует результат в хранилище "firebase-token-id"
     * Удаляет значение из кэша "firebase-token-user-name"
     */
    @Caching(
        put = [CachePut("firebase-token-id", key = "#entity.token")],
        evict = [CacheEvict("firebase-token-user-name", key = "#entity.user.username")]
    )
    override fun <S : FirebaseToken> save(entity: S): S

    /**
     * Удаляет токен Firebase [entity] из системы.
     * Удаляет значение из кэша "firebase-token-id"
     * Удаляет все значения из кэша "firebase-token-user-name"
     */
    @Caching(
        evict = [
            CacheEvict("firebase-token-id", key = "#entity.token"),
            CacheEvict("firebase-token-user-name", allEntries = true),
        ]
    )
    override fun delete(entity: FirebaseToken)

    /**
     * Удаляет все токены Firebase из системы.
     * Удаляет все значения из кэша "firebase-token-id"
     * Удаляет все значения из кэша "firebase-token-user-name"
     */
    @Caching(
        evict = [
            CacheEvict("firebase-token-id", allEntries = true),
            CacheEvict("firebase-token-user-name", allEntries = true),
        ]
    )
    override fun deleteAll()
}