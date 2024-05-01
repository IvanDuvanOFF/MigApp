package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.User
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["users"])
interface UserRepository : JpaRepository<User, UUID> {

//    @Cacheable(key = "#username", unless = "#result == null")
    fun findUserByUsername(username: String): Optional<User>

//    @Cacheable(key = "#email", unless = "#result == null")
    fun findUserByEmail(email: String): Optional<User>

    @Cacheable(key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<User>

    @Caching(
        put = [
            CachePut(key = "#entity.id"),
            CachePut(key = "#entity.username"),
            CachePut(key = "#entity.email")
        ]
    )
    override fun <S : User> save(entity: S): S

    @Caching(
        evict = [
            CacheEvict(key = "entity.id"),
            CacheEvict(key = "entity.username"),
            CacheEvict(key = "entity.email")
        ]
    )
    override fun delete(entity: User)

    @CacheEvict(allEntries = true)
    override fun deleteAll()
}