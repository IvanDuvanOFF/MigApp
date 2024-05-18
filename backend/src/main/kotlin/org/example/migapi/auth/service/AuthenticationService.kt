package org.example.migapi.auth.service

import io.jsonwebtoken.JwtException
import jakarta.persistence.PersistenceException
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.*
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.core.domain.model.SpringUser
import org.example.migapi.auth.model.TotpCode
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.account.service.UserService
import org.example.migapi.orThrow
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthenticationService(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val totpService: TotpService,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val revokedTokenService: RevokedTokenService,
    @Autowired
    private val emailService: EmailService,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val migUtils: MigUtils
) {

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

        val totp = totpService.generateCode(user)
        emailService.sendTfaEmail(user.email, totp)

        user.block()
        userService.saveUser(user)

        println()

        return SignResponse(username = user.username)
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            TfaCodeExpiredException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun validateTotp(verificationRequest: VerificationRequest) = SignResponse(
        username = verificationRequest.verify()
            .tfaId
            .user
            .username
    )

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
        restoreRequest.verification
            .verify()
            .removeCode()
            .tfaId
            .user
            .activate()
            .apply {
                if (restoreRequest.passwords.password != restoreRequest.passwords.confirmation)
                    throw BadCredentialsException("Passwords are not the same")

                userService.saveUser(this)
            }
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
        verificationRequest.verify()
            .removeCode()
            .tfaId
            .user
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
        val user = userService.findUserByUsername(username)

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken, user))
            throw RefreshTokenExpiredException()

        return user.generateSignResponse(request)
    }

    @Throws(
        exceptionClasses = [
            UnauthorizedException::class,
            JwtException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun logout(request: HttpServletRequest) {
        val jwt = migUtils.extractJwt(request)
        val username = (SecurityContextHolder.getContext().authentication.principal as SpringUser).username

        val user = userService.findUserByUsername(username)

        revokedTokenService.revokeToken(jwt, user, jwtService.extractExpirationDate(jwt))
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            TfaCodeExpiredException::class,
            BadCredentialsException::class,
            PersistenceException::class
        ]
    )
    fun VerificationRequest.verify(): TotpCode {
        val user = userService.findUserByUsername(this.username)

        return user.let {
            val totp = totpService.findTfaByUser(it, this.code)
            totp.apply { validateCode().orThrow { TfaCodeExpiredException() } }
        }
    }

    fun User.generateSignResponse(request: HttpServletRequest): SignResponse {
        val jwt = jwtService.generateToken(this)
        val refreshToken = jwtService.generateRefreshToken(this)

        return SignResponse(
            token = jwt,
            refreshToken = refreshToken,
            tfaEnabled = tfaEnabled
        )
    }

    private fun TotpCode.validateCode() = this.expirationDate > LocalDateTime.now()

    fun TotpCode.removeCode() = this.apply { totpService.removeCode(this) }

    private fun User.block() = this.apply { this.isActive = false }

    private fun User.activate() = this.apply {
        if (isActive)
            throw UserAlreadyActivatedException("The account has been already restored")

        isActive = true
    }
}