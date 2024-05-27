package org.example.adminbackend.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "bd_type")
data class DatabaseType(
    @Id
    val name: String,

    @Column(name = "driver_name")
    val driverName: String
)
