package org.example.migapi.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.migapi.domain.model.Model

@Entity
@Table(name = "document_types")
data class DocumentType(
    @Id
    val name: String
) : Model