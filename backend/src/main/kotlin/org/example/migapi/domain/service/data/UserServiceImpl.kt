package org.example.migapi.domain.service.data

import jakarta.persistence.PersistenceException
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.domain.dto.auth.RefreshTokenRequest
import org.example.migapi.domain.dto.auth.SignRequest
import org.example.migapi.domain.dto.auth.SignResponse
import org.example.migapi.domain.dto.auth.VerificationRequest
import org.example.migapi.domain.dto.data.UserDto
import org.example.migapi.domain.model.SpringUser
import org.example.migapi.domain.model.entity.User
import org.example.migapi.domain.model.entity.VerificationToken
import org.example.migapi.domain.model.enums.ERole
import org.example.migapi.domain.service.security.JwtService
import org.example.migapi.domain.service.security.TotpService
import org.example.migapi.exception.*
import org.example.migapi.repository.RoleRepository
import org.example.migapi.repository.UserRepository
import org.example.migapi.repository.VerificationTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class UserServiceImpl(
    @Autowired
    private val dtoService: DtoService,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val roleRepository: RoleRepository,
    @Autowired
    private val verificationTokenRepository: VerificationTokenRepository,
    @Autowired
    private val passwordEncoder: PasswordEncoder,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val totpService: TotpService,
    @Value("\${mig.jwt.verification-expiration}")
    private val verificationTokenExpiration: Int
) : UserService {

    override fun saveUser(signRequest: SignRequest, request: HttpServletRequest): User {
        if (userExists(signRequest.login))
            throw UserAlreadyExistsException("Username or email is already taken")

        val role = roleRepository.findById(ERole.ROLE_USER.name)
            .orElseThrow { RoleNotFoundException("No role ${ERole.ROLE_USER.name} does not exist") }

        val user = User(
            username = signRequest.login,
            password = passwordEncoder.encode(signRequest.password),
            role = role,
            isActive = true,

            name = "",
            surname = "",
            birthday = LocalDate.now()
        )

        return userRepository.save(user)
    }

    override fun createUser(userDto: UserDto): User {
        if (userExists(userDto.username))
            throw UserAlreadyExistsException("Username is already exists")

        val user = dtoService.toUser(userDto).apply { password = passwordEncoder.encode(password) }

        return userRepository.save(user)
    }

    override fun blockUser(email: String?): User {
        if (email == null)
            throw BadCredentialsException("Email cannot be null")

        return userRepository.findUserByEmail(email)
            .orElseThrow { BadCredentialsException("User with email $email not found") }
            .block()
    }

    override fun restoreUser(token: String, password: String) {
        val verificationToken = findUserByVerificationToken(token)

        if (verificationToken.user.isActive)
            throw UserAlreadyActivatedException("The account has been already restored")
        verificationToken.user.isActive = true

        verificationTokenRepository.delete(verificationToken)
    }

    override fun signIn(signRequest: SignRequest): SignResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signRequest.login,
                signRequest.password
            )
        )

        val userDetails = authentication.principal as SpringUser

        val user = userRepository.findUserByUsername(userDetails.username).get()

        if (user.using2Fa) {
            totpService.generateCode(user)


            return SignResponse(tfaEnabled = true)
        }

        return user.generateSignResponse()
    }

    override fun verifyCode(verificationRequest: VerificationRequest): SignResponse {
        val user = userRepository.findUserByUsername(verificationRequest.username)
            .orElseThrow { BadCredentialsException("User with username ${verificationRequest.username} not found") }

        if (!totpService.validateCode(user, verificationRequest.code))
            throw BadCredentialsException("Code not correct")

        return user.generateSignResponse()
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): SignResponse {
        val username = jwtService.extractUsername(refreshTokenRequest.refreshToken)
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("No user found") }

        if (!jwtService.isTokenValid(refreshTokenRequest.refreshToken, user))
            throw VerificationTokenExpiredException("Token expired")

        return user.generateSignResponse()
    }

    override fun prepareEmail(user: User, url: String): SimpleMailMessage {
        val verificationToken = verificationTokenRepository.save(
            VerificationToken(
                expirationDate = LocalDateTime.now().plus(verificationTokenExpiration.toLong(), ChronoUnit.MILLIS),
                user = user
            )
        )

        val email = SimpleMailMessage()

        email.setTo(user.email)
        email.subject = "Registration confirmation"
        email.text = "$url/auth/restore/${verificationToken.token}"

        return email
    }

    @Throws(
        exceptionClasses = [
            VerificationTokenNotFoundException::class,
            VerificationTokenExpiredException::class,
            PersistenceException::class
        ]
    )
    private fun findUserByVerificationToken(token: String): VerificationToken {
        val verificationToken = verificationTokenRepository.findById(UUID.fromString(token))

        if (verificationToken.isEmpty)
            throw VerificationTokenNotFoundException("Verification token has not been found")
        if (verificationToken.get().expirationDate.isBefore(LocalDateTime.now()))
            throw VerificationTokenExpiredException("Verification token has been expired")

        return verificationToken.get()
    }

    private fun User.generateSignResponse(): SignResponse {
        val jwt = jwtService.generateToken(this)
        val refreshToken = jwtService.generateRefreshToken(HashMap(), this)

        return SignResponse(
            token = jwt,
            refreshToken = refreshToken
        )
    }

    @Throws(exceptionClasses = [PersistenceException::class])
    fun userExists(username: String): Boolean =
        userRepository.findUserByUsername(username).isPresent
}