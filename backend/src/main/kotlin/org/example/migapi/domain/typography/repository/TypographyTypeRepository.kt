package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.TypographyType
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyTypeRepository : JpaRepository<TypographyType, String> {
}