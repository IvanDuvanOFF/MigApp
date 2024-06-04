package org.example.migapi.domain.notification.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User

/**
 * Сущность токена Firebase
 *
 * @property token токен Firebase
 * @property user пользователь, к устройству которого относится [token]
 */
@Entity
@Table(name = "firebase_tokens")
data class FirebaseToken(
    @Id
    val token: String,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
) : Model
