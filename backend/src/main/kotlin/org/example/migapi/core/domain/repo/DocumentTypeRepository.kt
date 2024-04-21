package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.DocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTypeRepository : JpaRepository<DocumentType, String> {
}