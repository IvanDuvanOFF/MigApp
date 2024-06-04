package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.TypographyAndRest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Репозиторий для представления [TypographyAndRest]
 */
@Repository
interface TypographyAndRestRepository : JpaRepository<TypographyAndRest, UUID> {

    /**
     * Находит все офрмления, до дедлайна которых от [start] до [end] дней
     */
    fun findAllByRestBetween(start: Int = 0, end: Int = 7): List<TypographyAndRest>
}