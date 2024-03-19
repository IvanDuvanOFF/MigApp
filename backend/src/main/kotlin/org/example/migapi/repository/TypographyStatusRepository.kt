package org.example.migapi.repository

import org.example.migapi.domain.model.entity.TypographyStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TypographyStatusRepository : JpaRepository<TypographyStatus, String> {
}