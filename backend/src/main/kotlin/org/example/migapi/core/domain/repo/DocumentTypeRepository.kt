package org.example.migapi.core.domain.repo

import org.example.migapi.domain.typography.model.DocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTypeRepository : JpaRepository<DocumentType, String> {
}