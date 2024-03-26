package org.example.migapi.core.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.auth.exception.VerificationTokenExpiredException
import org.example.migapi.auth.exception.VerificationTokenNotFoundException
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.model.entity.VerificationToken
import org.example.migapi.core.domain.model.enums.ERole
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface UserService {
    @Throws(exceptionClasses = [PersistenceException::class])
    fun saveUser(userDto: UserDto): User

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun findUserByUsername(username: String): User

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

    @Throws(exceptionClasses = [BadCredentialsException::class, PersistenceException::class])
    fun createVerificationToken(email: String): VerificationToken

    @Throws(
        exceptionClasses = [
            VerificationTokenNotFoundException::class,
            VerificationTokenExpiredException::class,
            PersistenceException::class
        ]
    )
    fun deleteVerificationToken(token: String): VerificationToken

    @TestOnly
    @Throws(exceptionClasses = [PersistenceException::class])
    fun dropTable()

    fun enableTfa(id: UUID)
}