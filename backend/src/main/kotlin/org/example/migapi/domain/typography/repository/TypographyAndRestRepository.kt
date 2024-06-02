package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.TypographyAndRest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TypographyAndRestRepository : JpaRepository<TypographyAndRest, UUID> {
    fun findAllByRestBetween(start: Int = 0, end: Int = 7): List<TypographyAndRest>
}