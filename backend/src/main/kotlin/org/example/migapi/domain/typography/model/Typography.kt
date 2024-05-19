package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "typographies")
data class Typography(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(targetEntity = TypographyType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "typography_type")
    val typographyType: TypographyType,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "creation_date")
    val creationDate: LocalDate = LocalDate.now(),

    var link: String? = null,

    @ManyToOne(targetEntity = TypographyStatus::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    var status: TypographyStatus,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "typography_id")
    val documents: MutableSet<Document>
) : Model