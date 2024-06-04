package org.example.migapi.domain.account.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.migapi.core.domain.model.Model

/**
 * Сущность страны пользователя
 *
 * @property name название страны
 */
@Entity
@Table(name = "countries")
data class Country(
    @Id
    val name: String = "NONE"
) : Model