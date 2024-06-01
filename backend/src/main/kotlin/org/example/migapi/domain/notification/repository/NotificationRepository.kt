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

@Repository
@CacheConfig(cacheNames = ["notification-id", "notification-username", "notifications"])
interface NotificationRepository : JpaRepository<Notification, UUID> {

    @Cacheable("notification-username", key = "#username", unless = "#result.isEmpty()")
    fun findAllByUserUsername(username: String): List<Notification>

    @Cacheable("notification-id", key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<Notification>

    @Caching(
        put = [CachePut("notification-id", key = "#entity.id")],
        evict = [CacheEvict("notification-username", key = "#entity.user.username")]
    )
    override fun <S : Notification> save(entity: S): S

    @Caching(
        evict = [
            CacheEvict("notification-id", allEntries = true),
            CacheEvict("notification-username", key = "#username")
        ]
    )
    fun deleteAllByUserUsername(username: String)

    @Caching(
        evict = [
            CacheEvict("notification-id", key = "#entity.id"),
            CacheEvict("notification-username", key = "#entity.user.username")
        ]
    )
    override fun delete(entity: Notification)

    @Caching(
        evict = [
            CacheEvict("notification-id", key = "#entity.id"),
            CacheEvict("notification-username", allEntries = true)
        ]
    )
    override fun deleteById(id: UUID)

    @Caching(
        evict = [
            CacheEvict("notification-id", allEntries = true),
            CacheEvict("notification-username", allEntries = true)
        ]
    )
    override fun deleteAll()
}