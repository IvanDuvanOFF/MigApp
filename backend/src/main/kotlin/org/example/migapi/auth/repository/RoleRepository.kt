package org.example.migapi.auth.repository

import org.example.migapi.auth.model.Role
import org.example.migapi.core.domain.model.enums.ERole
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для работы с ролями пользователя [Role]
 */
@Repository
@CacheConfig(cacheNames = ["roles"])
interface RoleRepository : JpaRepository<Role, ERole> {

    /**
     * Сохраняет роль [entity].
     * Сохраняет результат в кэш "roles"
     */
    @Cacheable(key = "#entity.name")
    override fun <S : Role> save(entity: S): S

    /**
     * Находит роль [Role] по [id].
     * Сохраняет результат в кэш "roles"
     */
    @Cacheable(key = "#id", unless = "#result == null")
    override fun findById(id: ERole): Optional<Role>

    /**
     * Удаляет роль [entity].
     * Удаляет сущность [entity] из кэша "roles"
     */
    @CacheEvict(key = "#entity.name")
    override fun delete(entity: Role)
}