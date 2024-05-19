package org.example.migapi.domain.account.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.Passwords
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.service.JwtService
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.account.dto.StudentDto
import org.example.migapi.domain.account.dto.TfaTurnDto
import org.example.migapi.domain.account.service.StudentService
import org.example.migapi.getUsernameFromContext
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/profile")
class ProfileController(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val studentService: StudentService,
    @Autowired
    private val migUtils: MigUtils
) {

    @PatchMapping
    @Operation(
        summary = "Студент меняет пароль",
        description = "Пользователь отправляет старый и новый пароль",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь сменил пароль",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Пароли не совпадают",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Доступ запрещен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    @SecurityRequirement(name = "JWT")
    fun changePassword(passwords: Passwords, request: HttpServletRequest) {
        val jwt = migUtils.extractJwt(request)

        if (passwords.password != passwords.confirmation)
            throw BadCredentialsException("Passwords are not the same")

        val username = jwtService.extractUsername(jwt)

        studentService.updatePassword(username, passwords.password)
    }

    @PatchMapping("/tfa")
    @Operation(
        summary = "Студент включает / выключает",
        description = "Пользователь отправляет старый и новый пароль",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь сменил пароль",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Пароли не совпадают",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Доступ запрещен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    @SecurityRequirement(name = "JWT")
    fun turnTfa(tfaTurnDto: TfaTurnDto, request: HttpServletRequest): TfaTurnDto =
        studentService.turnTfa(getUsernameFromContext(), tfaTurnDto)

    @GetMapping("/{user_id}")
    @Operation(
        summary = "Получение учетной записи пользователя",
        description = "Пользователь отправляет свой id и получает свою учетную запись",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Учетная запись найдена",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Id неверного формата",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Доступ запрещен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    @SecurityRequirement(name = "JWT")
    fun getStudentProfile(@PathVariable("user_id") userId: String): StudentDto =
        studentService.getByUsernameAndId(getUsernameFromContext(), userId)
}