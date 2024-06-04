package org.example.migapi.domain.typography.event

import jakarta.persistence.PersistenceException
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.service.NotificationService
import org.example.migapi.domain.typography.model.TypographyStatus
import org.example.migapi.domain.typography.service.TypographyService
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * Листенер для события истеющего дедлайна на оформление [ExpiredTypographyEvent]
 */
@Component
class ExpiredTypographyEventListener(
    @Autowired
    private val typographyService: TypographyService,
    @Autowired
    private val notificationService: NotificationService,
    @Autowired
    private val migUtils: MigUtils
) : ApplicationListener<ExpiredTypographyEvent> {

    /**
     * Метод слушает событие [ExpiredTypographyEvent] и отправляет уведомление [Notification]
     * соответствующему пользователю [User].
     *
     * @throws NotFoundException
     * @throws PersistenceException
     */
    override fun onApplicationEvent(event: ExpiredTypographyEvent) {
        val typography = event.typographyAndRest

        val title: String
        val description: String
        val status: ENotificationStatus

        when (typography.rest) {
            0 -> {
                title = "Your application is expired!"
                description =
                    "Your application from ${migUtils.localDateToString(typography.creationDate)} " +
                            "has been expired. Get a call to ICD"
                status = ENotificationStatus.BAD
            }

            else -> {
                title = "Your application is ${typography.rest} day${if (typography.rest > 1) "s" else ""} left!"
                description =
                    "Your application from ${migUtils.localDateToString(typography.creationDate)} " +
                            "has ${typography.rest} day before expiration"
                status = ENotificationStatus.INFO
            }
        }

        val notification = Notification(
            id = UUID.randomUUID(),
            user = typography.user,
            title = title,
            description = description,
            date = LocalDateTime.now(),
            isViewed = false,
            status = status
        )

        notificationService.pushNotification(notification)

        val typo = typographyService.getById(typography.id.toString())
        typo.status = TypographyStatus("FAILED")
        typographyService.save(typo)
    }
}