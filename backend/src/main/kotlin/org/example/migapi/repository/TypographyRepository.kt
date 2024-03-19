package org.example.migapi.repository

import org.example.migapi.domain.model.entity.Typography
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TypographyRepository : JpaRepository<Typography, UUID> {
}