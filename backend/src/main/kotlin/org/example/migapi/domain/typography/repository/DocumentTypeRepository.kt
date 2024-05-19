package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.DocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTypeRepository : JpaRepository<DocumentType, String> {
}