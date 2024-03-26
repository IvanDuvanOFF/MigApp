package org.example.migapi.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.assertj.core.api.Assertions
import org.example.migapi.auth.dto.SignRequest
import org.example.migapi.auth.service.EmailService
import org.example.migapi.auth.service.TotpService
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.core.domain.service.UserService
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.regex.Matcher
import java.util.regex.Pattern

@SpringBootTest
//@DataJpaTest
@AutoConfigureMockMvc
class AuthenticationControllerTests(
    @Autowired
    private val gson: Gson,
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired
    private val userService: UserService,

    @MockBean
    private val totpService: TotpService,
    @MockBean
    private val emailService: EmailService
) {

    @BeforeEach
    fun clearDb() = userService.dropTable()

    @Test
    fun userCanSignInNoTfa() {
        val userDto = UserDto(
            username = "test",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "test"))
        }
            .andExpect {
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
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "incorrect password"))
        }
            .andExpect {
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
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true
        )
        userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest("incorrect", "test"))
        }
            .andExpect {
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
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name,
            isActive = true,
            tfaEnabled = true
        )
        val user = userService.saveUser(userDto)

        mockMvc.post("/api/auth/signing") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = gson.toJson(SignRequest(userDto.username, "test"))
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath(
                        "$.access_token",
                        Matchers.nullValue()
                    )
                }
                content {
                    jsonPath(
                        "$.refresh_token",
                        Matchers.nullValue()
                    )
                }
                content { jsonPath("$.tfa_enabled", Matchers.`is`(true)) }
            }


        Mockito.`when`(totpService.generateCode(user)).thenReturn("666777")
        Mockito.`when`(emailService.sendTfaEmail(user.email, "666777")).then { }
        Mockito.`when`(totpService.validateCode(user, ))

    }
}