package org.example.adminbackend.domain.model

import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "workspace_user")
data class WorkspaceUser(
    @EmbeddedId
    val id: WorkspaceUserId,

    @Column(name = "is_root")
    val isRoot: Boolean
) {
    @Embeddable
    class WorkspaceUserId(
        @Column(name = "workspace_id")
        val workspaceId: UUID,

        @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User
    ) : Serializable
}
