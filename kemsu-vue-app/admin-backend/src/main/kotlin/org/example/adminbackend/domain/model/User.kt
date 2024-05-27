package org.example.adminbackend.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "admin_user")
data class User(
    @Id
    val id: UUID,
    val username: String,
    val password: String,
    @Column(name = "is_active")
    val isActive: Boolean = true
)
