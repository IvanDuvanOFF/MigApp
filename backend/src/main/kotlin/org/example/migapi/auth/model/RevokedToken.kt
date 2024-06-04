package org.example.migapi.auth.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User
import java.time.LocalDateTime

/**
 * Сущность отозванного токена
 *
 * @property token отозванный jwt-токен [String]
 * @property user пользователь, который отозвал токен [User]
 * @property expirationDate срок годности токена, по его истечение токен удаляется из бд
 */
@Entity
@Table(name = "revoked_tokens")
data class RevokedToken(
    @Id
    val token: String,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime
) : Model