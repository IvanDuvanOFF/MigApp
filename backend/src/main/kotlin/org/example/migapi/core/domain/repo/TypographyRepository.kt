package org.example.migapi.core.domain.repo

import org.example.migapi.domain.typography.model.Typography
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TypographyRepository : JpaRepository<Typography, UUID> {
}