package org.example.migapi.core.domain.service

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
import org.example.migapi.core.domain.repo.RoleRepository
import org.example.migapi.core.domain.repo.UserRepository
import org.example.migapi.core.domain.repo.VerificationTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class UserServiceImpl(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val verificationTokenRepository: VerificationTokenRepository,
    @Autowired
    private val roleRepository: RoleRepository,
    @Value("\${mig.jwt.verification-expiration}")
    private val verificationTokenExpiration: Int
) : UserService {

    override fun saveUser(userDto: UserDto): User {
        val role = findRoleByName(ERole.ROLE_USER.name)

        val user = User(
            username = userDto.username,
            password = userDto.password,
            role = role,
            isActive = false
        )

        return userRepository.save(user)
    }

    override fun findUserByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { UserNotFoundException("User with username $username doesn't exists") }

    override fun userExists(username: String): Boolean =
        userRepository.findUserByUsername(username).isPresent

    override fun findRoleByName(roleName: String): Role = roleRepository.findById(roleName)
        .orElseThrow { RoleNotFoundException("No role ${ERole.ROLE_USER.name} does not exist") }

    override fun createVerificationToken(email: String): VerificationToken {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { BadCredentialsException("User with email $email not found") }

        return verificationTokenRepository.save(
            VerificationToken(
                expirationDate = LocalDateTime.now().plus(verificationTokenExpiration.toLong(), ChronoUnit.MILLIS),
                user = user
            )
        )
    }

    override fun deleteVerificationToken(token: String): VerificationToken {
        val verificationToken = verificationTokenRepository.findById(UUID.fromString(token))

        if (verificationToken.isEmpty)
            throw VerificationTokenNotFoundException("Verification token has not been found")
        if (verificationToken.get().expirationDate.isBefore(LocalDateTime.now()))
            throw VerificationTokenExpiredException("Verification token has been expired")

        verificationTokenRepository.delete(verificationToken.get())

        return verificationToken.get()
    }
}