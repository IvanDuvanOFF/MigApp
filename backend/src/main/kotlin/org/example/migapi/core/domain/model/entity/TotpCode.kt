package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "tfa_codes")
data class TotpCode(
    @EmbeddedId
    val tfaId: TotpCodeId,

    @Column(name = "expiration_date", nullable = false)
    val expirationDate: LocalDateTime
) : Model {
    @Embeddable
    class TotpCodeId(
        val code: String,

        @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User
    ) : Serializable
}
