package org.example.migapi.core.domain.model.entity

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

    val expirationDate: LocalDate,

    var link: String? = null,
) : Model