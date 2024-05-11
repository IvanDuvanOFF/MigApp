package org.example.migapi.core.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.model.enums.ERole
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service
import java.util.*

@Service
interface UserService {

    @TestOnly
    @Throws(exceptionClasses = [PersistenceException::class])
    fun saveUser(userDto: UserDto): User

    fun saveUser(user: User): User

    @Throws(
        exceptionClasses = [
            PersistenceException::class,
            IllegalArgumentException::class,
            UserNotFoundException::class
        ]
    )
    fun findById(id: String): User

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun findUserByUsername(username: String): User

    @Throws(exceptionClasses = [PersistenceException::class])
    fun findUsersByRole(roleName: ERole): List<User>

    @Throws(exceptionClasses = [PersistenceException::class])
    fun userExists(username: String): Boolean

    @Throws(
        exceptionClasses = [
            RoleNotFoundException::class,
            PersistenceException::class,
            IllegalArgumentException::class
        ]
    )
    fun findRoleByName(roleName: String): Role

    @Throws(exceptionClasses = [RoleNotFoundException::class, PersistenceException::class])
    fun findRoleByERole(roleEnum: ERole): Role

    fun deleteUserById(id: String)

    @TestOnly
    @Throws(exceptionClasses = [PersistenceException::class])
    fun dropTable()

    fun enableTfa(id: UUID)
}