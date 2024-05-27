package org.example.adminbackend.domain.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user_table")
data class UserTable(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = Config::class, fetch = FetchType.LAZY)
    val config: Config,

    val name: String
)
