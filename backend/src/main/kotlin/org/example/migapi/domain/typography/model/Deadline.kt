package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.Country
import java.io.Serializable

/**
 * Сущность дедлайна
 *
 * @property deadlineId комплексный id, состоящий из типа оформления [DeadlineId.typographyType]
 *  и страны, для которой этот дедлайн актуален [DeadlineId.country]
 * @property days  количество дней, в течение которых необходимо завершить оформление типа [DeadlineId.typographyType]
 */
@Entity
@Table(name = "deadlines")
data class Deadline(
    @EmbeddedId
    val deadlineId: DeadlineId,

    val days: Int
) : Model {
    @Embeddable
    class DeadlineId(
        @Column(name = "typography_type")
        val typographyType: String,

        @ManyToOne(targetEntity = Country::class, fetch = FetchType.EAGER)
        @JoinColumn(name = "country")
        val country: Country,
    ) : Serializable
}