package org.example.adminbackend.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "attribute_type")
data class AttributeType(
    @Id
    val name: String,
    val description: String,

    @ManyToOne(targetEntity = DatabaseType::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "bd_type")
    val databaseType: DatabaseType
)
