package org.example.migapi.domain.notification.repository

import org.example.migapi.domain.notification.model.FirebaseToken
import org.example.migapi.domain.notification.model.Notification
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["notification-id"])
interface NotificationRepository : JpaRepository<Notification, UUID> {

    @Query("select n from Notification n where n.token = ?1")
    fun findAllNotificationsByToken(token: FirebaseToken): List<Notification>

    @Cacheable("notification-id", key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<Notification>

    @CachePut("notification-id", key = "#entity.id")
    override fun <S : Notification> save(entity: S): S

    @CacheEvict("notification-id", key = "#entity.id")
    override fun delete(entity: Notification)

    @CacheEvict("notification-id", key = "#id")
    override fun deleteById(id: UUID)

    @CacheEvict("notification-id", allEntries = true)
    override fun deleteAll()
}