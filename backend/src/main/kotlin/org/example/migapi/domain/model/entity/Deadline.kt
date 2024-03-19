package org.example.migapi.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.domain.model.Model
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