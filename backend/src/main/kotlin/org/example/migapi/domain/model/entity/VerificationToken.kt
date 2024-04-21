package org.example.migapi.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.domain.model.Model
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "verification_tokens")
data class VerificationToken(
    @Id
    val token: UUID = UUID.randomUUID(),

    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    val user: User
) : Model