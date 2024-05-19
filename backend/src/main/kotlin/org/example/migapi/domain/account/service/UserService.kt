package org.example.migapi.domain.account.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.domain.account.dto.UserDto
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.auth.model.Role
import org.example.migapi.domain.account.model.User
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.domain.files.exception.NoAccessException
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

    @Throws(exceptionClasses = [UserNotFoundException::class, NoAccessException::class, PersistenceException::class])
    fun findUserByUsernameAndId(username: String, id: String): User

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun findUserByEmail(email: String): User

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun findUserByUsernameOrEmail(usernameOrEmail: String): User

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun findUserByEmailOrPhone(emailOrPhone: String): User

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun findUserByPhone(phone: String): User

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