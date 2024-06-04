package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model

/**
 * Тип
 */
@Entity
@Table(name = "typography_types")
data class TypographyType(
    @Id
    val name: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "typography_documents",
        joinColumns = [JoinColumn(name = "typography_type", referencedColumnName = "name")],
        inverseJoinColumns = [JoinColumn(name = "document_type", referencedColumnName = "name")])
    val documentList: MutableSet<DocumentType>
) : Model