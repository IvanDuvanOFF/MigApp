package org.example.adminbackend.domain.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "property")
data class Property(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = PropertyType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    val type: PropertyType,

    @Column(name = "config_id")
    val configId: UUID,

    val value: String
)
