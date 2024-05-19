package org.example.migapi.domain.notification.service

import com.google.api.gax.rpc.InvalidArgumentException
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.notification.dto.NotificationDto
import org.example.migapi.domain.notification.exception.NotificationNotFoundException
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.repository.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class NotificationService(
    @Autowired
    private val notificationRepository: NotificationRepository
) {

    @Throws(
        exceptionClasses = [
            NotFoundException::class,
            BadRequestException::class,
            PersistenceException::class
        ]
    )
    fun findAllByUsername(username: String): List<NotificationDto> {
        val notifications = notificationRepository.findAllByUserUsername(username)

        return notifications.map { it.toDto() }
    }

    fun createNewNotification(user: User, notificationDto: NotificationDto): Notification {
        val notification = Notification(
            id = UUID.randomUUID(),
            user = user,
            title = notificationDto.title ?: throw BadRequestException(),
            description = notificationDto.description ?: throw BadRequestException(),
            date = LocalDateTime.now(),
            isViewed = false,
            status = ENotificationStatus.INFO
        )

        return notification
    }

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

    fun Notification.toDto() = NotificationDto(
        id = this.id.toString(),
        title = this.title,
        description = this.description,
        date = Date.from(
            this.date
                .atZone(ZoneId.systemDefault())
                .toInstant()
        ),
        isViewed = this.isViewed,
        status = this.status.name
    )
}