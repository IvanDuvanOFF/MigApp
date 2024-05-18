package org.example.migapi.auth.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User
import java.time.LocalDateTime

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