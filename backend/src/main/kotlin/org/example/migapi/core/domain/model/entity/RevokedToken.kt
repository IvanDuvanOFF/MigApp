package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
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