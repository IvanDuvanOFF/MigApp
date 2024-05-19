package org.example.migapi.domain.notification.service

import com.google.api.gax.rpc.InvalidArgumentException
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.domain.notification.exception.NotificationNotFoundException
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.repository.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService(
    @Autowired
    private val notificationRepository: NotificationRepository
) {

    @Throws(exceptionClasses = [BadRequestException::class, NotificationNotFoundException::class, PersistenceException::class])
    fun findNotificationById(id: String): Notification {
        val uuid = try {
            UUID.fromString(id)
        } catch (e: InvalidArgumentException) {
            throw BadRequestException("Invalid id")
        }

        return notificationRepository.findById(uuid).orElseThrow { NotificationNotFoundException() }
    }

    @Transactional
    @Throws(exceptionClasses = [BadRequestException::class, NotificationNotFoundException::class, PersistenceException::class])
    fun changeNotificationViewed(id: String, isViewed: Boolean): Notification {
        val notification = findNotificationById(id)
        notification.isViewed = isViewed

        return notificationRepository.save(notification)
    }

    @Transactional
    @Throws(exceptionClasses = [BadRequestException::class, NotificationNotFoundException::class, PersistenceException::class])
    fun deleteNotification(id: String) = notificationRepository.deleteById(
        try {
            UUID.fromString(id)
        } catch (e: InvalidArgumentException) {
            throw BadRequestException("Invalid id")
        }
    )
}