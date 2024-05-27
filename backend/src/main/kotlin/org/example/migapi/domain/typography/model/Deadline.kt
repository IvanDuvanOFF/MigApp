package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.Country
import java.io.Serializable

@Entity
@Table(name = "deadlines")
data class Deadline(
    @EmbeddedId
    val deadlineId: DeadlineId,

    val days: Int
) : Model {
    @Embeddable
    class DeadlineId(
        @ManyToOne(targetEntity = TypographyType::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "typography_type")
        val typographyType: TypographyType,

        @ManyToOne(targetEntity = Country::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "country")
        val country: Country,
    ) : Serializable
}