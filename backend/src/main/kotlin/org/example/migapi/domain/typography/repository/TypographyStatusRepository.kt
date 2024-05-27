package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.TypographyStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyStatusRepository : JpaRepository<TypographyStatus, String> {
}