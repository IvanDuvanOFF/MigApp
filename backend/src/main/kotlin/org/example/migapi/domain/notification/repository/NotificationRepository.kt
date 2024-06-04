package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.notification.model.Notification
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для уведомлений
 */
@Repository
@CacheConfig(cacheNames = ["notification-id", "notification-username", "notifications"])
interface NotificationRepository : JpaRepository<Notification, UUID> {

    /**
     * Метод находит все уведомления [List]<[Notification]> для конкретного пользователя по его [username].
     * Затем сохраняет полученный список в кэш "notification-username"
     */
    @Cacheable("notification-username", key = "#username", unless = "#result.isEmpty()")
    fun findAllByUserUsername(username: String): List<Notification>

    /**
     * Метод находит уведомление [Notification] по его id [id].
     * Затем сохраняет его в кэш "notification-id"
     */
    @Cacheable("notification-id", key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<Notification>

    /**
     * Сохраняет уведомление в систему.
     * Добавляет его в кэш "notification-id". И удаляет из кэша "notification-username" значение по ключу
     */
    @Caching(
        put = [CachePut("notification-id", key = "#entity.id")],
        evict = [CacheEvict("notification-username", key = "#entity.user.username")]
    )
    override fun <S : Notification> save(entity: S): S

    /**
     * Удаляет все уведомления пользователя из системы.
     * Очищает полностью кэш "notification-id".
     * Удаляет значение из кэша "notification-username" значение по ключу
     */
    @Caching(
        evict = [
            CacheEvict("notification-id", allEntries = true),
            CacheEvict("notification-username", key = "#username")
        ]
    )
    fun deleteAllByUserUsername(username: String)

    /**
     * Удаляет уведомление пользователя из системы.
     * Удаляет значение из кэша "notification-username" значение по ключу
     * Удаляет значение из кэша "notification-id" значение по ключу
     */
    @Caching(
        evict = [
            CacheEvict("notification-id", key = "#entity.id"),
            CacheEvict("notification-username", key = "#entity.user.username")
        ]
    )
    override fun delete(entity: Notification)

    /**
     * Удаляет уведомление пользователя из системы по его id [id].
     * Очищает полностью кэш "notification-username".
     * Удаляет значение из кэша "notification-id" значение по ключу
     */
    @Caching(
        evict = [
            CacheEvict("notification-id", key = "#entity.id"),
            CacheEvict("notification-username", allEntries = true)
        ]
    )
    override fun deleteById(id: UUID)

    /**
     * Удаляет абсолютно все уведомления из системы.
     * Очищает полностью кэш "notification-username".
     * Очищает полностью кэш "notification-id".
     */
    @Caching(
        evict = [
            CacheEvict("notification-id", allEntries = true),
            CacheEvict("notification-username", allEntries = true)
        ]
    )
    override fun deleteAll()
}