package org.example.migapi.domain.notification.model

import com.google.firebase.messaging.Message
import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.notification.config.FirebaseNotification
import java.time.LocalDateTime
import java.util.*

/**
 * Сущность уведомления
 *
 * @property id id уведомления в системе [UUID]
 * @property user пользователь, которому отправлено уведомление [User]
 * @property title заголовок [String]
 * @property description описание уведомления [String]
 * @property date дата и время отправки уведомления [LocalDateTime]
 * @property isViewed флаг просмотрено уведомление или нет [Boolean]
 * @property status статус уведомления [ENotificationStatus]
 */
@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    val title: String,

    val description: String? = null,

    @Column(name = "notification_date")
    val date: LocalDateTime,

    @Column(name = "is_viewed")
    var isViewed: Boolean,

    @Enumerated(EnumType.STRING)
    var status: ENotificationStatus
) : Model {

    /**
     * Метод создания объекта уведомления [Message] для Firebase
     */
    fun message(token: FirebaseToken): Message {
        val firebaseNotification = FirebaseNotification
            .builder()
            .setTitle(title)
            .setBody(description)
            .build()

        return Message.builder()
            .setNotification(firebaseNotification)
            .setToken(token.token)
            .putAllData(
                mutableMapOf(
                    "date" to date.toString(),
                    "status" to status.name,
                    "is_viewed" to isViewed.toString()
                )
            )
            .build()
    }
}
