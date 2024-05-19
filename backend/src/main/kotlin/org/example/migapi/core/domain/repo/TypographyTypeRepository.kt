package org.example.migapi.core.domain.repo

import org.example.migapi.domain.typography.model.TypographyType
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyTypeRepository : JpaRepository<TypographyType, String> {
}