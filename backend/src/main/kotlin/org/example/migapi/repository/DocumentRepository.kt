package org.example.migapi.repository

import org.example.migapi.domain.model.entity.Document
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DocumentRepository : JpaRepository<Document, UUID> {
}