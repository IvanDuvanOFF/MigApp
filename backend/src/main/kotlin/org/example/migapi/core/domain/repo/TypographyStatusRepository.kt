package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.TypographyStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyStatusRepository : JpaRepository<TypographyStatus, String> {
}