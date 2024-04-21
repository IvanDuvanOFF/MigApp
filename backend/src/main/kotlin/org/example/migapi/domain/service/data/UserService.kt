package org.example.migapi.domain.service.data

import io.jsonwebtoken.JwtException
import jakarta.persistence.PersistenceException
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.domain.dto.data.UserDto
import org.example.migapi.domain.dto.auth.RefreshTokenRequest
import org.example.migapi.domain.dto.auth.SignRequest
import org.example.migapi.domain.dto.auth.SignResponse
import org.example.migapi.domain.model.entity.User
import org.example.migapi.exception.*
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException

interface UserService {
    @Throws(
        exceptionClasses = [
            UserAlreadyExistsException::class,
            RoleNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun saveUser(signRequest: SignRequest, request: HttpServletRequest): User

    @Throws(
        exceptionClasses = [
            UserAlreadyExistsException::class,
            RoleNotFoundException::class,
            CountryNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun createUser(userDto: UserDto): User

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            DisabledException::class,
            LockedException::class,
            PersistenceException::class
        ]
    )
    fun signIn(signRequest: SignRequest): SignResponse

    @Throws(
        exceptionClasses = [JwtException::class,
            TokenExpiredException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): SignResponse

    @Throws(
        exceptionClasses = [
            UsernameNullException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun blockUser(email: String?): User

    @Throws(exceptionClasses = [PersistenceException::class])
    fun prepareEmail(user: User, url: String): SimpleMailMessage

    @Throws(
        exceptionClasses = [
            UserAlreadyActivatedException::class,
            VerificationTokenNotFoundException::class,
            VerificationTokenExpiredException::class,
            PersistenceException::class
        ]
    )
    fun restoreUser(token: String, password: String)
}