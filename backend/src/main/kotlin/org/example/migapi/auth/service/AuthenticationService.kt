package org.example.migapi.auth.service

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
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val emailService: EmailService,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val migUtils: MigUtils,
) {

    companion object {
        const val REMOTE_ADDRESS_NAME = "remote-address"
    }

    @Throws(
        exceptionClasses = [
            UserBlockedException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun blockUser4Restore(emailOrPhone: BlockRequest): SignResponse {

        val user = when {
            emailOrPhone.email.isNotEmpty() && migUtils.isEmail(emailOrPhone.email) ->
                userService.findUserByEmail(emailOrPhone.email)

            emailOrPhone.phone.isNotEmpty() && migUtils.isPhone(emailOrPhone.phone) ->
                userService.findUserByPhone(emailOrPhone.phone)

            else -> throw BadCredentialsException("Email or phone are incorrect")
        }

        if (user.isActive.not())
            throw UserBlockedException()

        val totp = totpService.generateCode(user)
        emailService.sendTfaEmail(user.email, totp)

        user.block()
        userService.saveUser(user)

        return SignResponse(username = user.username)
    }

    @Throws(
        exceptionClasses = [
            UserAlreadyActivatedException::class,
            UserNotFoundException::class,
            TfaCodeExpiredException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun restoreUser(restoreRequest: RestoreRequest) {
        val user = userService.findUserByUsername(restoreRequest.verification.username)

        if (user.isActive)
            throw UserAlreadyActivatedException()

        user.verifyTfaCode(restoreRequest.verification)

        if (restoreRequest.passwords.password != restoreRequest.passwords.confirmation)
            throw BadCredentialsException("Passwords are not the same")

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

            return SignResponse(
                username = user.username,
                tfaEnabled = true
            )
        }

        return user.generateSignResponse(request)
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            TfaCodeExpiredException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun verifyTfa(verificationRequest: VerificationRequest, request: HttpServletRequest): SignResponse =
        userService.findUserByUsername(verificationRequest.username)
            .verifyTfaCode(verificationRequest)
            .generateSignResponse(request)


    @Throws(
        exceptionClasses = [
            JwtException::class,
            TfaCodeExpiredException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest, request: HttpServletRequest): SignResponse {
        val username = jwtService.extractUsername(refreshTokenRequest.refreshToken)
        val remoteIp = migUtils.getRemoteAddress(request)

        val user = userService.findUserByUsername(username)

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken, user, remoteIp))
            throw RefreshTokenExpiredException()

        return user.generateSignResponse(request)
    }

    fun User.generateSignResponse(request: HttpServletRequest): SignResponse {
        val remoteIp = migUtils.getRemoteAddress(request)

        val extraClaims = mutableMapOf(REMOTE_ADDRESS_NAME to remoteIp)

        val jwt = jwtService.generateToken(this, extraClaims)
        val refreshToken = jwtService.generateRefreshToken(this, extraClaims)

        return SignResponse(
            token = jwt,
            refreshToken = refreshToken,
            tfaEnabled = tfaEnabled
        )
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            TfaCodeExpiredException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    private fun User.verifyTfaCode(verificationRequest: VerificationRequest): User {
        if (!totpService.validateCode(this, verificationRequest.code))
            throw BadCredentialsException("Code not correct")

        return this
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