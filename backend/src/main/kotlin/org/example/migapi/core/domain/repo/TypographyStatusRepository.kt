package org.example.migapi.core.domain.repo

import org.example.migapi.domain.typography.model.TypographyStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyStatusRepository : JpaRepository<TypographyStatus, String> {
}