package org.example.migapi.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
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
    private lateinit var gson: Gson

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

    @Test
    fun userCanSignInNoTfa() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "test"))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.access_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content {
                jsonPath(
                    "$.refresh_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(false)) }
        }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectPassword() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "incorrect password"))
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(400)
                )
            }
        }
    }

    @Test
    fun userCannotSignInNoTfaIncorrectLogin() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest("incorrect", "test"))
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(404)
                )
            }
        }
    }

    @Test
    fun userCanSignInTfa() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true,
            tfaEnabled = true
        )
        val user = userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.access_token") { doesNotExist() } }
            content { jsonPath("$.refresh_token") { doesNotExist() } }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(true)) }
        }

        mockMvc.post("/api/auth/signing/tfa") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(VerificationRequest(user.username, totp))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.access_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content {
                jsonPath(
                    "$.refresh_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(true)) }
        }
    }

    @Test
    fun userCannotSignInTfaIncorrectUsername() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true,
            tfaEnabled = true
        )
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.access_token") { doesNotExist() } }
            content { jsonPath("$.refresh_token") { doesNotExist() } }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(true)) }
        }

        mockMvc.post("/api/auth/signing/tfa") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(VerificationRequest("fake", totp))
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(404)
                )
            }
        }
    }

    @Test
    fun userCannotSignInTfaIncorrectCode() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true,
            tfaEnabled = true
        )
        userService.saveUser(userDto)
        val request = SignRequest(userDto.username, "test")

        val totp = "666777"

        Mockito.`when`(totpService.validateCode(any(), eq(totp))).thenReturn(true)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(true)) }
        }

        mockMvc.post("/api/auth/signing/tfa") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(VerificationRequest(request.login, "fake"))
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(400)
                )
            }
        }
    }

    @Test
    fun userCanRefreshToken() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        val refreshToken = mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "test"))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(false)) }
        }.andReturn().response.contentAsString.let {
            return@let mapper.readValue(it, SignResponse::class.java)
        }.refreshToken

        mockMvc.post("/api/auth/refresh") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(RefreshTokenRequest(refreshToken))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.access_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content {
                jsonPath(
                    "$.refresh_token",
                    Matchers.matchesPattern("^([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+=.]+)\\.([A-Za-z0-9-_+/=]*)\$")
                )
            }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(false)) }
        }
    }

    @Test
    fun userCannotRefreshTokenIncorrectToken() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "test"))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(false)) }
        }

        mockMvc.post("/api/auth/refresh") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(RefreshTokenRequest("fake"))
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(400)
                )
            }
        }
    }

    @Test
    fun userCannotRefreshTokenExpired() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        val refreshToken = mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(SignRequest(userDto.username, "test"))
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.tfa_enabled", Matchers.`is`(false)) }
        }.andReturn().response.contentAsString.let {
            return@let mapper.readValue(it, SignResponse::class.java)
        }.refreshToken

        Thread.sleep((refreshExpiration * 3).toLong())

        mockMvc.post("/api/auth/refresh") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(RefreshTokenRequest(refreshToken))
        }.andExpect {
            status { isGone() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath(
                    "$.status.code",
                    Matchers.`is`(410)
                )
            }
        }
    }

    @Test
    fun userCanRestore() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        val token = UUID.randomUUID()
        var user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(mutableMapOf("email" to userDto.email))
        }.andExpect { status { isOk() } }

        user = userService.findUserByUsername(userDto.username)
        Assertions.assertThat(user.isActive).isFalse()

        Mockito.`when`(verificationTokenService.deleteVerificationToken(eq(token.toString())))
            .thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore/$token") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Passwords("newpass", "newpass"))
        }.andExpect { status { isOk() } }
    }

    @Test
    fun userCannotRestoreIncorrectPasswords() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        val token = UUID.randomUUID()
        var user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(mutableMapOf("email" to userDto.email))
        }.andExpect { status { isOk() } }

        user = userService.findUserByUsername(userDto.username)
        Assertions.assertThat(user.isActive).isFalse()

        mockMvc.post("/api/auth/restore/$token") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Passwords("newpass", "fakepass"))
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun userCannotRestoreUserRestored() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(mutableMapOf("email" to userDto.email))
        }.andExpect { status { isOk() } }

        user.isActive = true

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore/$token") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Passwords("newpass", "newpass"))
        }.andExpect { status { isConflict() } }
    }

    @Test
    fun userCannotRestoreTokenNotFound() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(mutableMapOf("email" to userDto.email))
        }.andExpect { status { isOk() } }

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any()))
            .thenThrow(VerificationTokenNotFoundException("Token not found"))

        mockMvc.post("/api/auth/restore/$token") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Passwords("newpass", "newpass"))
        }.andExpect { status { isNotFound() } }
    }

    @Test
    fun userCannotRestoreTokenExpired() {
        val userDto = UserDto(
            username = "test",
            email = "test@test.com",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        val token = UUID.randomUUID()
        val user = userService.saveUser(userDto)
        val verificationToken = VerificationToken(
            token = token,
            expirationDate = LocalDateTime.now().plus(verificationExpiration.toLong(), ChronoUnit.MILLIS),
            user = user
        )

        Mockito.`when`(verificationTokenService.createVerificationToken(any())).thenReturn(verificationToken)

        mockMvc.post("/api/auth/restore") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(mutableMapOf("email" to userDto.email))
        }.andExpect { status { isOk() } }

        Mockito.`when`(verificationTokenService.deleteVerificationToken(any()))
            .thenThrow(VerificationTokenExpiredException("Verification token has been expired"))

        mockMvc.post("/api/auth/restore/$token") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Passwords("newpass", "newpass"))
        }.andExpect { status { isGone() } }
    }
}