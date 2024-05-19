package org.example.migapi.core.domain.repo

import org.example.migapi.domain.typography.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DocumentRepository : JpaRepository<Document, UUID> {
}