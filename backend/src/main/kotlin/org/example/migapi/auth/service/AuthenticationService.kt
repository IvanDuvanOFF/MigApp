package org.example.migapi.auth.service

import io.jsonwebtoken.JwtException
import jakarta.persistence.PersistenceException
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.*
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.SpringUser
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.UserService
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val totpService: TotpService,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val emailService: EmailService,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val migUtils: MigUtils,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @Throws(
        exceptionClasses = [
            UserAlreadyExistsException::class,
            RoleNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun registerUser(signRequest: SignRequest, request: HttpServletRequest): User {
        if (userService.userExists(signRequest.login))
            throw UserAlreadyExistsException("Username or email is already taken")

        val userDto = UserDto(
            username = signRequest.login,
            password = passwordEncoder.encode(signRequest.password),
            role = ERole.ROLE_USER.name
        )

        return userService.saveUser(userDto)
    }

    @Throws(
        exceptionClasses = [
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun blockUser4Restore(email: String, httpServletRequest: HttpServletRequest) {
        val verificationToken = userService.createVerificationToken(email)
        verificationToken.user.block()

        emailService.sendRestoreEmail(
            email,
            verificationToken.token.toString(),
            migUtils.getHostUrl(httpServletRequest)
        )
    }

    @Throws(
        exceptionClasses = [
            UserAlreadyActivatedException::class,
            VerificationTokenNotFoundException::class,
            VerificationTokenExpiredException::class,
            PersistenceException::class
        ]
    )
    fun restoreUser(token: String, passwords: Passwords) {
        if (passwords.password != passwords.confirmation)
            throw BadCredentialsException()

        userService.deleteVerificationToken(token).user.activate()
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            BadCredentialsException::class,
            DisabledException::class,
            LockedException::class,
            PersistenceException::class
        ]
    )
    fun authenticate(signRequest: SignRequest): SignResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signRequest.login,
                signRequest.password
            )
        )

        val userDetails = authentication.principal as SpringUser

        val user = userService.findUserByUsername(userDetails.username)

        if (user.tfaEnabled) {
            val totp = totpService.generateCode(user)
            emailService.sendTfaEmail(user.email, totp)

            return SignResponse(tfaEnabled = true)
        }

        return user.generateSignResponse()
    }

    @Throws(
        exceptionClasses = [
            BadCredentialsException::class,
            TfaNotEnabledException::class,
            PersistenceException::class
        ]
    )
    fun verifyTfa(verificationRequest: VerificationRequest): SignResponse {
        val user = userService.findUserByUsername(verificationRequest.username)

        if (user.tfaEnabled.not())
            throw TfaNotEnabledException()

        if (!totpService.validateCode(user, verificationRequest.code))
            throw BadCredentialsException("Code not correct")

        return user.generateSignResponse()
    }

    @Throws(
        exceptionClasses = [
            JwtException::class,
            VerificationTokenExpiredException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): SignResponse {
        val username = jwtService.extractUsername(refreshTokenRequest.refreshToken)

        val user = userService.findUserByUsername(username)

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken, user))
            throw VerificationTokenExpiredException("Token expired")

        return user.generateSignResponse()
    }

    fun User.generateSignResponse(): SignResponse {
        val jwt = jwtService.generateToken(this)
        val refreshToken = jwtService.generateRefreshToken(HashMap(), this)

        return SignResponse(
            token = jwt,
            refreshToken = refreshToken,
            tfaEnabled = tfaEnabled
        )
    }

    private fun User.block() {
        this.isActive = this.isActive.not()
    }

    private fun User.activate() {
        if (isActive)
            throw UserAlreadyActivatedException("The account has been already restored")

        isActive = true
    }
}