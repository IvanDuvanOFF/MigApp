package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Typography
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TypographyRepository : JpaRepository<Typography, UUID> {
}