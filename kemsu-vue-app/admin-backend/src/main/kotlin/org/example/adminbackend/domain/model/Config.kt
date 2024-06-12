package org.example.adminbackend.domain.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "config")
data class Config(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = Workspace::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    val workspace: Workspace,

    @Column(name = "bd_url")
    val databaseUrl: String,

    @ManyToOne(targetEntity = DatabaseType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "bd_type")
    val databaseType: DatabaseType,

    @OneToMany(targetEntity = Property::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "config_id")
    val properties: MutableList<Property>
)
