package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.enums.ERole

@Entity
@Table(name = "roles")
class Role(
    @Id
    @Enumerated(EnumType.STRING)
    val name: ERole
) : Model