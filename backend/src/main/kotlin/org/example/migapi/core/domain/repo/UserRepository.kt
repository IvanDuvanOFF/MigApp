package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.entity.User
import org.springframework.cache.annotation.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(
    cacheNames = [
        "user-username",
        "user-email",
        "user-phone",
        "user-id",
        "user-role"
    ]
)
interface UserRepository : JpaRepository<User, UUID> {

    @Cacheable("user-username", key = "#username", unless = "#result == null")
    fun findUserByUsername(username: String): Optional<User>

    @Cacheable("user-email", key = "#email", unless = "#result == null")
    fun findUserByEmail(email: String): Optional<User>

    @Cacheable("user-phone", key = "#phone", unless = "#result == null")
    fun findUserByPhone(phone: String): Optional<User>

    @Cacheable("user-id", key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<User>

    @Cacheable(
        "user-role",
        key = "#role.name.name()",
        condition = "#role.name.name() != 'ROLE_ADMIN'",
        unless = "#result.isEmpty()"
    )
    fun findUsersByRole(role: Role): List<User>

    @Caching(
        put = [
            CachePut("user-id", key = "#entity.id"),
            CachePut("user-username", key = "#entity.username"),
            CachePut("user-email", key = "#entity.email"),
            CachePut("user-phone", key = "#entity.phone")
        ],
        evict = [CacheEvict("user-role", allEntries = true)]
    )
    override fun <S : User> save(entity: S): S

    @Caching(
        evict = [
            CacheEvict("user_id", key = "#entity.id"),
            CacheEvict("user-username", key = "#entity.username"),
            CacheEvict("user-email", key = "#entity.email"),
            CacheEvict("user-phone", key = "#entity.phone"),
            CacheEvict("user-role", allEntries = true)
        ]
    )
    override fun delete(entity: User)

    @CacheEvict(allEntries = true)
    override fun deleteAll()
}