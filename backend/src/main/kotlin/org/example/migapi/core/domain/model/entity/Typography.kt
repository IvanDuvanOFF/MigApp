package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "typography_documents",
        joinColumns = [JoinColumn(name = "typography_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "document_id", referencedColumnName = "id")])
    val documents: MutableSet<Document>
) : Model