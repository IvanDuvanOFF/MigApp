package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "tfa_codes")
data class TfaCode(
    @EmbeddedId
    val tfaId: TfaCodeId,

    @Column(name = "expiration_date", nullable = false)
    val expirationDate: LocalDateTime
) {
    @Embeddable
    class TfaCodeId(
        val code: String,

        @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User
    ) : Serializable
}
