package org.example.adminbackend.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "attribute")
data class Attribute(
    @EmbeddedId
    val id: AttributeId,

    @ManyToOne(targetEntity = AttributeType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    val type: AttributeType,

    val length: Int? = null,

    @Column(name = "is_primary_key")
    val isPrimaryKey: Boolean = false,

    @Column(name = "is_unique")
    val isUnique: Boolean = false,

    @Column(name = "is_nullable")
    val isNullable: Boolean = true,

    @Column(name = "reference_table")
    val referenceTable: String,

    @Column(name = "reference_column")
    val referenceColumn: String,

    @Column(name = "check_constraint")
    val checkConstraint: String
) {
    @Embeddable
    class AttributeId(
        @Column(name = "name")
        val name: String,

        @ManyToOne(targetEntity = UserTable::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "table_id")
        val table: UserTable
    ) : Serializable
}
