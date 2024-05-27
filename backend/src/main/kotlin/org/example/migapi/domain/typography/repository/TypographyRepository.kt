package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.Typography
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["typography-user-name", "typography-id"])
interface TypographyRepository : JpaRepository<Typography, UUID> {

    @Cacheable("typography-user-name", key = "#username")
    fun findAllByUserUsername(username: String): List<Typography>

    @Cacheable("typography-id", key = "#id")
    override fun findById(id: UUID): Optional<Typography>

    @Caching(
        evict = [
            CacheEvict("typography-user-name", allEntries = true),
            CacheEvict("typography-id", allEntries = true)
        ]
    )
    override fun delete(entity: Typography)
}