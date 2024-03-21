package org.example.migapi.auth

import org.example.migapi.auth.controller.AuthenticationController
import org.example.migapi.auth.dto.SignRequest
import org.example.migapi.auth.service.AuthenticationService
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.core.domain.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTests(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired
    private val dtoService: DtoService,
    @MockBean
    private val userService: UserService
) {
    @Test
    fun userCanSignInNoTfa() {
        val userDto = UserDto(
            username = "test",
            password = passwordEncoder.encode("test"),
            role = ERole.ROLE_USER.name
        )
        val user = dtoService.userDtoToUser(userDto)

        Mockito.`when`(userService.findUserByUsername(user.username)).thenReturn(user)

        mockMvc.post("api/auth/signing", SignRequest(userDto.username, userDto.password))
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("$.access_token") { isNotEmpty() } }
            .andExpect { jsonPath("$.tfa_enabled") { value(false) } }
    }
}