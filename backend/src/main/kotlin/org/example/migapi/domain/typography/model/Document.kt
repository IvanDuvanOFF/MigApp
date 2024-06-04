package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import java.time.LocalDate
import java.util.*

/**
 * Сущность документа
 *
 * @property id id документа формата [UUID]
 * @property documentType тип документа [DocumentType]
 * @property typographyId id типа оформления [UUID]
 * @property status статус документа в системе [DocumentStatus]
 * @property creationDate дата создания документа [LocalDate]
 * @property expirationDate дата истечения срока действия документа [LocalDate]
 * @property fileName имя файла документа [String]
 */
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