package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.TypographyType
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyTypeRepository : JpaRepository<TypographyType, String> {
}