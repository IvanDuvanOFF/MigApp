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

    var status: String,

    val expirationDate: LocalDate,

    var link: String? = null,
) : Model