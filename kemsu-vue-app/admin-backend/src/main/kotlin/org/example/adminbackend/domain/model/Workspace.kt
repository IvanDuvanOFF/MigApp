package org.example.adminbackend.domain.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "workspace")
data class Workspace(
    @Id
    val id: UUID,
    val name: String = "Untitled",

    @OneToMany(targetEntity = WorkspaceUser::class,fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    val users: MutableList<WorkspaceUser>
)
