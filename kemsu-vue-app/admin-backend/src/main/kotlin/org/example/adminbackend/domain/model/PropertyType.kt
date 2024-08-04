package org.example.adminbackend.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "property_type")
data class PropertyType(
    @Id
    val name: String
)
