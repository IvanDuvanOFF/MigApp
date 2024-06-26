package org.example.migapi.domain.notification.service

import com.google.api.gax.rpc.InvalidArgumentException
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.service.UserService
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.dto.NotificationDto
import org.example.migapi.domain.notification.exception.NotificationNotFoundException
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.repository.NotificationRepository
import org.example.migapi.getUsernameFromContext
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * Сервис для работы с уведомлениями пользователей
 */
@Service
class NotificationService(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val notificationRepository: NotificationRepository,
    @Autowired
    private val notificationSendService: NotificationSendService,
    @Autowired
    private val migUtils: MigUtils
) {

    /**
     * Метод для добавления токена Firebase [FirebaseTokenDto] в систему и привязки его к пользователю по его id
     */
    fun addFirebaseToken(firebaseTokenDto: FirebaseTokenDto) =
        notificationSendService.addFirebaseToken(firebaseTokenDto)

    fun pushNotification(notificationDto: NotificationDto): Notification {
        val user = userService.findUserByUsername(getUsernameFromContext())

        val notification = Notification(
            id = UUID.randomUUID(),
            user = user,
            title = notificationDto.title ?: throw BadRequestException(),
            description = notificationDto.description ?: throw BadRequestException(),
            date = LocalDateTime.now(),
            isViewed = false,
            status = try {
                notificationDto.status?.uppercase()?.let { ENotificationStatus.valueOf(it) } ?: ENotificationStatus.INFO
            } catch (e: Exception) {
                throw BadRequestException()
            }
        )

        notificationRepository.save(notification)
        notificationSendService.sendNotification(notification)

        return notification
    }

    fun pushNotification(notification: Notification) {
        notificationRepository.save(notification)
        notificationSendService.sendNotification(notification)
    }

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
    fun deleteNotification(id: String) {
        val notification = findNotificationById(id)
        notificationRepository.delete(notification)
    }

    fun Notification.toDto() = NotificationDto(
        id = this.id.toString(),
        title = this.title,
        description = this.description,
        date = migUtils.localDateTimeToString(this.date),
        isViewed = this.isViewed,
        status = this.status.name
    )
}