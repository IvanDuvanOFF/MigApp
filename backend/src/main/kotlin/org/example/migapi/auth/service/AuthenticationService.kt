package org.example.migapi.auth.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import jakarta.persistence.PersistenceException
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.*
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.SpringUser
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.service.UserService
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val totpService: TotpService,
    @Autowired
    private val verificationTokenService: VerificationTokenService,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val emailService: EmailService,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val migUtils: MigUtils,
) {

    private val logger = KotlinLogging.logger {  }

    companion object {
        const val REMOTE_ADDRESS_NAME = "remote-address"
    }

    @Throws(
        exceptionClasses = [
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun blockUser4Restore(email: String, httpServletRequest: HttpServletRequest) {
        val verificationToken = verificationTokenService.createVerificationToken(email)
        verificationToken.user.block()
        userService.saveUser(verificationToken.user)

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

        val user = verificationTokenService.deleteVerificationToken(token).user
        user.activate()

        userService.saveUser(user)
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
    fun authenticate(signRequest: SignRequest, request: HttpServletRequest): SignResponse {
        request.headerNames.toList().forEach {
            logger.warn { "HEADER $it = ${request.getHeader(it)}" }
        }

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

        return user.generateSignResponse(request)
    }

    @Throws(
        exceptionClasses = [
            BadCredentialsException::class,
            TfaNotEnabledException::class,
            PersistenceException::class
        ]
    )
    fun verifyTfa(verificationRequest: VerificationRequest, request: HttpServletRequest): SignResponse {
        val user = userService.findUserByUsername(verificationRequest.username)

        if (user.tfaEnabled.not())
            throw TfaNotEnabledException()

        if (!totpService.validateCode(user, verificationRequest.code))
            throw BadCredentialsException("Code not correct")

        return user.generateSignResponse(request)
    }

    @Throws(
        exceptionClasses = [
            JwtException::class,
            VerificationTokenExpiredException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest, request: HttpServletRequest): SignResponse {
        val username = jwtService.extractUsername(refreshTokenRequest.refreshToken)
        val remoteIp = migUtils.getRemoteAddress(request)

        val user = userService.findUserByUsername(username)

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken, user, remoteIp))
            throw VerificationTokenExpiredException("Token expired")

        return user.generateSignResponse(request)
    }

    fun User.generateSignResponse(request: HttpServletRequest): SignResponse {
        val remoteIp = migUtils.getRemoteAddress(request)
        logger.info { "REMOTE IP ADDRESS = $remoteIp" }

        val extraClaims = mutableMapOf(REMOTE_ADDRESS_NAME to remoteIp)

        val jwt = jwtService.generateToken(this, extraClaims)
        val refreshToken = jwtService.generateRefreshToken(this, extraClaims)

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