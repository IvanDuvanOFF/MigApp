package org.example.migapi.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.VerificationTokenExpiredException
import org.example.migapi.auth.exception.VerificationTokenNotFoundException
import org.example.migapi.auth.service.EmailService
import org.example.migapi.auth.service.TotpService
import org.example.migapi.auth.service.VerificationTokenService
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.model.entity.VerificationToken
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.UserService
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTests(
    @Value("\${mig.jwt.refresh-expiration}")
    private val refreshExpiration: Int,

    @Value("\${mig.jwt.verification-expiration}")
    private val verificationExpiration: Int
) {
    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @MockBean
    private lateinit var totpService: TotpService

    @MockBean
    private lateinit var emailService: EmailService

    @MockBean
    private lateinit var verificationTokenService: VerificationTokenService

    @BeforeEach
    fun clearDb() = userService.dropTable()

    companion object {
        const val JWT_REGEX = "^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$"
    }

    fun generateTestUser(isActive: Boolean = true, tfaEnabled: Boolean = false): UserDto = UserDto(
        username = "test",
        email = "test@test.test",
        password = passwordEncoder.encode("test"),
        role = ERole.ROLE_USER.name,
        isActive = isActive,
        tfaEnabled = tfaEnabled
    )

    fun performRequest(url: String, requestBody: Any?): ResultActionsDsl = mockMvc.post(url) {
        contentType = MediaType.APPLICATION_JSON
        accept = MediaType.APPLICATION_JSON
        content = mapper.writeValueAsString(requestBody)
    }

    @Test
    fun userCanSignInNoTfa() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        performRequest("/api/auth/signing", SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.access_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.refresh_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.tfa_enabled") { value(false) } }
            }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectPassword() {
        val userDto = generateTestUser(true)
        userService.saveUser(userDto)

        performRequest("/api/auth/signing", SignRequest(userDto.username, "incorrect password"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(400) } }
            }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectLogin() {
        val userDto = generateTestUser(true)
        userService.saveUser(userDto)

        performRequest("/api/auth/signing", SignRequest("incorrect", "test"))
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(404) } }
            }
    }

    @Test
    fun userCanSignInTfa() {
        val userDto = generateTestUser(isActive = true, tfaEnabled = true)
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        performRequest("/api/auth/signing", request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.access_token") { doesNotExist() } }
                content { jsonPath("$.refresh_token") { doesNotExist() } }
                content { jsonPath("$.tfa_enabled") { value(true) } }
            }

        performRequest("/api/auth/signing/tfa", VerificationRequest(userDto.username, totp))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.access_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.refresh_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.tfa_enabled") { value(true) } }
            }
    }

    @Test
    fun userCannotSignInTfaIncorrectUsername() {
        val userDto = generateTestUser(isActive = true, tfaEnabled = true)
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        performRequest("/api/auth/signing", request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.access_token") { doesNotExist() } }
                content { jsonPath("$.refresh_token") { doesNotExist() } }
                content { jsonPath("$.tfa_enabled") { value(true) } }
            }

        performRequest("/api/auth/signing/tfa", VerificationRequest("fake", totp))
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(404) } }
            }
    }

    @Test
    fun userCannotSignInTfaIncorrectCode() {
        val userDto = generateTestUser(true, tfaEnabled = true)
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"
        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        performRequest("/api/auth/signing", request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.tfa_enabled") { value(true) } }
            }

        performRequest("/api/auth/signing/tfa", VerificationRequest(request.login, "fake"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(400) } }
            }
    }

    @Test
    fun userCanRefreshToken() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val refreshToken = performRequest("/api/auth/signing", SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.tfa_enabled") { value(false) } }
            }.andReturn().response.contentAsString.let {
                return@let mapper.readValue(it, SignResponse::class.java)
            }.refreshToken

        performRequest("/api/auth/refresh", RefreshTokenRequest(refreshToken))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.access_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.refresh_token", Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath("$.tfa_enabled") { value(false) } }
            }
    }

    @Test
    fun userCannotRefreshTokenIncorrectToken() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        performRequest("/api/auth/signing", SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.tfa_enabled") { value(false) } }
            }

        performRequest("/api/auth/refresh", RefreshTokenRequest("fake"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(400) } }
            }
    }

    @Test
    fun userCannotRefreshTokenExpired() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val refreshToken = performRequest("/api/auth/signing", SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.tfa_enabled") { value(false) } }
            }.andReturn().response.contentAsString.let {
                return@let mapper.readValue(it, SignResponse::class.java)
            }.refreshToken

        Thread.sleep((refreshExpiration * 3).toLong())

        performRequest("/api/auth/refresh", RefreshTokenRequest(refreshToken))
            .andExpect {
                status { isGone() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("$.status.code") { value(410) } }
            }
    }

    @Test
    fun userCanRestore() {
        val userDto = generateTestUser()
        val user = userService.saveUser(userDto)
        val token = UUID.randomUUID()
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore", mutableMapOf("email" to userDto.email)).andExpect { status { isOk() } }

        Assertions.assertThat(user.isActive).isFalse()

        Mockito.`when`(verificationTokenService.deleteVerificationToken(eq(token.toString())))
            .thenReturn(verificationToken)

        performRequest("/api/auth/restore/$token", Passwords("newpass", "newpass")).andExpect { status { isOk() } }
    }

    @Test
    fun userCannotRestoreIncorrectPasswords() {
        val userDto = generateTestUser()
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore", mutableMapOf("email" to userDto.email)).andExpect { status { isOk() } }

        Assertions.assertThat(user.isActive).isFalse()

        performRequest("/api/auth/restore/$token", Passwords("newpass", "fakepass"))
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun userCannotRestoreUserRestored() {
        val userDto = generateTestUser()
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore", mutableMapOf("email" to userDto.email)).andExpect { status { isOk() } }

        user.isActive = true

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore/$token", Passwords("newpass", "newpass"))
            .andExpect { status { isConflict() } }
    }

    @Test
    fun userCannotRestoreTokenNotFound() {
        val userDto = generateTestUser()
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore", mutableMapOf("email" to userDto.email)).andExpect { status { isOk() } }

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any()))
            .thenThrow(VerificationTokenNotFoundException("Token not found"))

        performRequest("/api/auth/restore/$token", Passwords("newpass", "newpass"))
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun userCannotRestoreTokenExpired() {
        val userDto = generateTestUser()
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        performRequest("/api/auth/restore", mutableMapOf("email" to userDto.email)).andExpect { status { isOk() } }

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any()))
            .thenThrow(VerificationTokenExpiredException("Verification token has been expired"))

        performRequest("/api/auth/restore/$token", Passwords("newpass", "newpass")).andExpect { status { isGone() } }
    }
}