package org.example.migapi.domain.account.repository

import org.example.migapi.auth.model.Role
import org.example.migapi.domain.account.model.User
import org.springframework.cache.annotation.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Репозиторий для работы с пользователями [User]
 */
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

    /**
     * Находит пользователя [User] по его имени [username].
     * Кэширует результат в хранилище "user-username".
     */
    @Cacheable("user-username", key = "#username", unless = "#result == null")
    fun findUserByUsername(username: String): Optional<User>

    /**
     * Находит пользователя [User] по его email [email].
     * Кэширует результат в хранилище "user-email".
     */
    @Cacheable("user-email", key = "#email", unless = "#result == null")
    fun findUserByEmail(email: String): Optional<User>

    /**
     * Находит пользователя [User] по его номеру телефона [phone].
     * Кэширует результат в хранилище "user-phone".
     */
    @Cacheable("user-phone", key = "#phone", unless = "#result == null")
    fun findUserByPhone(phone: String): Optional<User>

    /**
     * Находит пользователя [User] по его id [id].
     * Кэширует результат в хранилище "user-id".
     */
    @Cacheable("user-id", key = "#id", unless = "#result == null")
    override fun findById(id: UUID): Optional<User>

    /**
     * Находит всех пользователей [List]<[User]> по их роли [role].
     * Кэширует результат в хранилище "user-role".
     */
    @Cacheable(
        "user-role",
        key = "#role.name.name()",
        condition = "#role.name.name() != 'ROLE_ADMIN'",
        unless = "#result.isEmpty()"
    )
    fun findUsersByRole(role: Role): List<User>

    /**
     * Сохраняет сущность пользователя [entity] в систему.
     * Сохраняет в кэши "user-id", "user-username", "user-email", "user-phone".
     * Удаляет из кэша "user-role", "tfa_user_id".
     */
    @Caching(
        put = [
            CachePut("user-id", key = "#entity.id"),
            CachePut("user-username", key = "#entity.username"),
            CachePut("user-email", key = "#entity.email"),
            CachePut("user-phone", key = "#entity.phone")
        ],
        evict = [
            CacheEvict("user-role", allEntries = true),
            CacheEvict("tfa_user_id", key = "#entity.id")
        ]
    )
    override fun <S : User> save(entity: S): S

    /**
     * Удаляет сущность пользователя [entity] из системы.
     * Удаляет из кэша "user-role", "user-id", "user-username", "user-email", "user-phone".
     */
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

    /**
     * Удаляет все сущности пользователей из системы.
     * Очищает все кэши.
     */
    @CacheEvict(allEntries = true)
    override fun deleteAll()
}