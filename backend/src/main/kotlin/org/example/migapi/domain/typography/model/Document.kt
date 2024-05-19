package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "documents")
data class Document(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(targetEntity = DocumentType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "document_type")
    val documentType: DocumentType,

    @Column(name = "typography_id")
    val typographyId: UUID,

    @Enumerated(EnumType.STRING)
    var status: DocumentStatus,

    @Column(name = "creation_date")
    val creationDate: LocalDate? = null,

    @Column(name = "expiration_date")
    val expirationDate: LocalDate? = null,

    @Column(name = "file_name")
    var fileName: String? = null,
) : Model