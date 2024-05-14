package org.example.migapi.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.service.EmailService
import org.example.migapi.auth.service.TotpService
import org.example.migapi.auth.service.VerificationTokenService
import org.example.migapi.config.TestRedisConfiguration
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.model.entity.TotpCode
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
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime


@SpringBootTest(classes = [TestRedisConfiguration::class])
@AutoConfigureMockMvc
@EnableCaching
@ImportAutoConfiguration(classes = [
    CacheAutoConfiguration::class,
    RedisAutoConfiguration::class
])
class AuthenticationControllerTests(
    @Value("\${mig.jwt.refresh-expiration}")
    private val refreshExpiration: Int
) {
    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

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

        private const val BASIC_URL = "/api/auth"

        const val SIGN_URL = "$BASIC_URL/signing"
        const val SIGN_TFA_URL = "$BASIC_URL/tfa"
        const val REFRESH_URL = "$BASIC_URL/refresh"
        const val RESTORE_URL = "$BASIC_URL/restore"
        const val BLOCK_URL = "$RESTORE_URL/block"

        const val ACCESS_TOKEN = "$.access_token"
        const val REFRESH_TOKEN = "$.refresh_token"
        const val TFA_ENABLED = "$.tfa_enabled"
        const val STATUS_CODE = "$.status.code"
        const val USERNAME = "$.username"
    }

    fun generateTestUser(isActive: Boolean = true, tfaEnabled: Boolean = false): UserDto = UserDto(
        username = "test",
        email = "test@test.test",
        password = "test",
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

        performRequest(SIGN_URL, SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(ACCESS_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(REFRESH_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(TFA_ENABLED) { value(false) } }
            }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectPassword() {
        val userDto = generateTestUser(true)
        userService.saveUser(userDto)

        performRequest(SIGN_URL, SignRequest(userDto.username, "incorrect password"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(400) } }
            }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectLogin() {
        val userDto = generateTestUser(true)
        userService.saveUser(userDto)

        performRequest(SIGN_URL, SignRequest("incorrect", "test"))
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(404) } }
            }
    }

    @Test
    fun userCanSignInTfa() {
        val userDto = generateTestUser(isActive = true, tfaEnabled = true)
        val user = userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"
        val totpCode = TotpCode(
            tfaId = TotpCode.TotpCodeId(code = totp, user),
            expirationDate = LocalDateTime.now().plusHours(1)
        )

        Mockito.`when`(totpService.findTfaByUser(any(), eq(totp))).thenReturn(totpCode)

        performRequest(SIGN_URL, request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(ACCESS_TOKEN) { doesNotExist() } }
                content { jsonPath(REFRESH_TOKEN) { doesNotExist() } }
                content { jsonPath(TFA_ENABLED) { value(true) } }
            }

        performRequest(SIGN_TFA_URL, VerificationRequest(userDto.username, totp))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(ACCESS_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(REFRESH_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(TFA_ENABLED) { value(true) } }
            }
    }

    @Test
    fun userCannotSignInTfaIncorrectUsername() {
        val userDto = generateTestUser(isActive = true, tfaEnabled = true)
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.findTfaByUser(any(), any())).thenThrow(BadCredentialsException())

        performRequest(SIGN_URL, request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(ACCESS_TOKEN) { doesNotExist() } }
                content { jsonPath(REFRESH_TOKEN) { doesNotExist() } }
                content { jsonPath(TFA_ENABLED) { value(true) } }
            }

        performRequest(SIGN_TFA_URL, VerificationRequest("fake", totp))
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(404) } }
            }
    }

    @Test
    fun userCannotSignInTfaIncorrectCode() {
        val userDto = generateTestUser(true, tfaEnabled = true)
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        Mockito.`when`(totpService.findTfaByUser(any(), any())).thenThrow(BadCredentialsException())

        performRequest(SIGN_URL, request)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(TFA_ENABLED) { value(true) } }
            }

        performRequest(SIGN_TFA_URL, VerificationRequest(request.login, "fake"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(400) } }
            }
    }

    @Test
    fun userCanRefreshToken() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val refreshToken = performRequest(SIGN_URL, SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(TFA_ENABLED) { value(false) } }
            }.andReturn().response.contentAsString.let {
                return@let mapper.readValue(it, SignResponse::class.java)
            }.refreshToken

        performRequest(REFRESH_URL, RefreshTokenRequest(refreshToken))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(ACCESS_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(REFRESH_TOKEN, Matchers.matchesPattern(JWT_REGEX)) }
                content { jsonPath(TFA_ENABLED) { value(false) } }
            }
    }

    @Test
    fun userCannotRefreshTokenIncorrectToken() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        performRequest(SIGN_URL, SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(TFA_ENABLED) { value(false) } }
            }

        performRequest(REFRESH_URL, RefreshTokenRequest("fake"))
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(400) } }
            }
    }

    @Test
    fun userCannotRefreshTokenExpired() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val refreshToken = performRequest(SIGN_URL, SignRequest(userDto.username, "test"))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(TFA_ENABLED) { value(false) } }
            }.andReturn().response.contentAsString.let {
                return@let mapper.readValue(it, SignResponse::class.java)
            }.refreshToken

        Thread.sleep((refreshExpiration * 3).toLong())

        performRequest(REFRESH_URL, RefreshTokenRequest(refreshToken))
            .andExpect {
                status { isGone() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath(STATUS_CODE) { value(410) } }
            }
    }

    @Test
    fun userCanRestore() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val blockRequest = BlockRequest(email = userDto.email)

        val totp = "666777"

        Mockito.`when`(totpService.generateCode(any())).thenReturn(totp)

        performRequest(BLOCK_URL, blockRequest).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath(USERNAME, Matchers.matchesPattern(userDto.username)) }
        }

        val user = userService.findUserByUsername(userDto.username)
        val totpCode = TotpCode(
            tfaId = TotpCode.TotpCodeId(code = totp, user),
            expirationDate = LocalDateTime.now().plusHours(1)
        )
        Mockito.`when`(totpService.findTfaByUser(any(), eq(totp))).thenReturn(totpCode)

        Assertions.assertThat(user.isActive).isFalse()

        val restoreRequest = RestoreRequest(
            verification = VerificationRequest(username = userDto.username, code = totp),
            passwords = Passwords("newpass", "newpass")
        )

        performRequest(RESTORE_URL, restoreRequest).andExpect { status { isOk() } }
    }

    @Test
    fun userCannotRestoreIncorrectPasswords() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val blockRequest = BlockRequest(email = userDto.email)

        val totp = "666777"

        Mockito.`when`(totpService.generateCode(any())).thenReturn(totp)

        performRequest(BLOCK_URL, blockRequest).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath(USERNAME, Matchers.matchesPattern(userDto.username)) }
        }

        val user = userService.findUserByUsername(userDto.username)
        Assertions.assertThat(user.isActive).isFalse()

        val totpCode = TotpCode(
            TotpCode.TotpCodeId(totp, user),
            expirationDate = LocalDateTime.now().plusHours(1)
        )
        Mockito.`when`(totpService.findTfaByUser(any(), eq(totp))).thenReturn(totpCode)

        val restoreRequest = RestoreRequest(
            verification = VerificationRequest(username = userDto.username, code = totp),
            passwords = Passwords("newpass", "fakepass")
        )

        performRequest(RESTORE_URL, restoreRequest).andExpect { status { isBadRequest() } }
    }

    @Test
    fun userCannotRestoreUserRestored() {
        val userDto = generateTestUser()
        val user = userService.saveUser(userDto)

        val blockRequest = BlockRequest(email = userDto.email)

        val totp = "666777"

        Mockito.`when`(totpService.generateCode(any())).thenReturn(totp)

        performRequest(BLOCK_URL, blockRequest).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath(USERNAME, Matchers.matchesPattern(userDto.username)) }
        }

        val totpCode = TotpCode(
            tfaId = TotpCode.TotpCodeId(code = totp, user),
            expirationDate = LocalDateTime.now().plusHours(1)
        )
        Mockito.`when`(totpService.findTfaByUser(any(), eq(totp))).thenReturn(totpCode)

        Assertions.assertThat(user.isActive).isTrue()

        val restoreRequest = RestoreRequest(
            verification = VerificationRequest(username = userDto.username, code = totp),
            passwords = Passwords("newpass", "newpass")
        )

        performRequest(RESTORE_URL, restoreRequest).andExpect { status { isConflict() } }
    }

    @Test
    fun userCannotRestoreTokenNotFound() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val blockRequest = BlockRequest(email = userDto.email)

        val totp = "666777"

        Mockito.`when`(totpService.generateCode(any())).thenReturn(totp)

        performRequest(BLOCK_URL, blockRequest).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath(USERNAME, Matchers.matchesPattern(userDto.username)) }
        }

        val user = userService.findUserByUsername(userDto.username)
        Mockito.`when`(totpService.findTfaByUser(any(), any())).thenThrow(BadCredentialsException())

        Assertions.assertThat(user.isActive).isFalse()

        val restoreRequest = RestoreRequest(
            verification = VerificationRequest(username = userDto.username, code = ""),
            passwords = Passwords("newpass", "newpass")
        )

        performRequest(RESTORE_URL, restoreRequest).andExpect { status { isBadRequest() } }
    }

    @Test
    fun userCannotRestoreTokenExpired() {
        val userDto = generateTestUser()
        userService.saveUser(userDto)

        val blockRequest = BlockRequest(email = userDto.email)

        val totp = "666777"

        Mockito.`when`(totpService.generateCode(any())).thenReturn(totp)

        performRequest(BLOCK_URL, blockRequest).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath(USERNAME, Matchers.matchesPattern(userDto.username)) }
        }

        val user = userService.findUserByUsername(userDto.username)
        Assertions.assertThat(user.isActive).isFalse()

        val totpCode = TotpCode(
            tfaId = TotpCode.TotpCodeId(code = totp, user),
            expirationDate = LocalDateTime.now()
        )
        Mockito.`when`(totpService.findTfaByUser(any(), any())).thenReturn(totpCode)

        val restoreRequest = RestoreRequest(
            verification = VerificationRequest(username = userDto.username, code = totp),
            passwords = Passwords("newpass", "newpass")
        )

        performRequest(RESTORE_URL, restoreRequest).andExpect { status { isGone() } }
    }
}