package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.enums.ERole
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["roles"])
interface RoleRepository : JpaRepository<Role, ERole> {

    @Cacheable(key = "#entity.name")
    override fun <S : Role> save(entity: S): S

    @Cacheable(key = "#id", unless = "#result == null")
    override fun findById(id: ERole): Optional<Role>

    @CacheEvict(key = "#entity.name")
    override fun delete(entity: Role)
}