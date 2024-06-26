package org.example.migapi.auth.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.enums.ERole

/**
 * Сущность роли
 *
 * @property name имя роли
 */
@Entity
@Table(name = "roles")
class Role(
    @Id
    @Enumerated(EnumType.STRING)
    val name: ERole
) : Model