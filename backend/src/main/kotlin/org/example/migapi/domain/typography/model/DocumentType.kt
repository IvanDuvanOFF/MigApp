package org.example.migapi.domain.typography.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.migapi.core.domain.model.Model

/**
 * Сущность типа документа.
 *
 * @property name название типа документа
 */
@Entity
@Table(name = "document_types")
data class DocumentType(
    @Id
    val name: String
) : Model