package org.example.migapi.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.domain.model.Model
import org.example.migapi.domain.model.enums.ERole

@Entity
@Table(name = "roles")
class Role(
    @Id
    @Enumerated(EnumType.STRING)
    val name: ERole
) : Model