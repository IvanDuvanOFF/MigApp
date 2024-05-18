package org.example.migapi.domain.notification.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.enums.ENotificationStatus
import org.example.migapi.domain.account.model.User
import java.time.LocalDateTime
import java.util.UUID

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
    val isViewed: Boolean,

    @Enumerated(EnumType.STRING)
    val status: ENotificationStatus
)
