package org.example.migapi.repository

import org.example.migapi.domain.model.entity.DocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTypeRepository : JpaRepository<DocumentType, String> {
}