package org.example.migapi.domain.notification.model

import com.google.firebase.messaging.Message
import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.notification.config.FirebaseNotification
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = FirebaseToken::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_token")
    val token: FirebaseToken,

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
    fun message(): Message {
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
