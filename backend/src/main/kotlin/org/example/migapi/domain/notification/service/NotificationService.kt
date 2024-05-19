package org.example.migapi.domain.notification.service

import com.google.api.gax.rpc.InvalidArgumentException
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.domain.notification.dto.NotificationDto
import org.example.migapi.domain.notification.exception.NotificationNotFoundException
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.repository.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class NotificationService(
    @Autowired
    private val notificationRepository: NotificationRepository,
    @Autowired
    private val firebaseTokenService: FirebaseTokenService
) {

    @Throws(
        exceptionClasses = [
            NotFoundException::class,
            BadRequestException::class,
            PersistenceException::class,
            IllegalArgumentException::class]
    )
    fun findAllByUserId(userId: String): List<NotificationDto> {
        val firebaseToken = firebaseTokenService.findByUserId(userId)

        return notificationRepository.findAllNotificationsByToken(firebaseToken).map { it.toDto() }
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