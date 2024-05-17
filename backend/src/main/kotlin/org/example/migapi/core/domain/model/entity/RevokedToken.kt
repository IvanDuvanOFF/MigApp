package org.example.migapi.core.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "revoked_token")
class RevokedToken(
    @Id
    val token: String,
    val expirationDate: LocalDateTime
)