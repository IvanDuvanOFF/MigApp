package org.example.migapi.domain.account.repository

import org.example.migapi.domain.account.model.StudentStatus
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["student-status-id"])
interface StudentStatusRepository : JpaRepository<StudentStatus, String> {

    @Cacheable("student-status-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<StudentStatus>
}