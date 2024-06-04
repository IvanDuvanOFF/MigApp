package org.example.migapi.domain.typography.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.migapi.core.domain.model.Model

/**
 * Статус оформления в сситеме
 *
 * @property name название статуса оформления
 *
 * - DONE оформление готово
 * - FAILED оформление не готово в срок
 * - IN_PROGRESS оформление в процессе
 * - LOADING оформление на проверке
 */
@Entity
@Table(name = "typography_statuses")
data class TypographyStatus(
    @Id
    val name: String
) : Model