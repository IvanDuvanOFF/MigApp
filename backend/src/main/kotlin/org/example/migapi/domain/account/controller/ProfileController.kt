package org.example.migapi.domain.account.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.account.dto.*
import org.example.migapi.domain.account.service.StudentService
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "Endpoints for working with user's account data")
class ProfileController(
    @Autowired
    private val studentService: StudentService
) {

    @PatchMapping("password")
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
    fun changePassword(@RequestBody @Valid passwordDto: PasswordDto, request: HttpServletRequest) {
        val username = getUsernameFromContext()

        studentService.updatePassword(username, passwordDto)
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
    fun turnTfa(@RequestBody tfaTurnDto: TfaTurnDto, request: HttpServletRequest): TfaTurnDto =
        studentService.turnTfa(getUsernameFromContext(), tfaTurnDto)

    @GetMapping
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
    fun getStudentProfile(): StudentDto {
        val username = getUsernameFromContext()

        return studentService.getByUsername(username)
    }

    @PatchMapping("phone")
    @Operation(
        summary = "Студент меняет номер телефона",
        description = "Пользователь отправляет новый номер телефона",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь сменил телефон",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Номер некорректного формата",
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
    fun changePhone(@RequestBody phoneDto: PhoneDto) = studentService.changePhone(getUsernameFromContext(), phoneDto)

    @PatchMapping("email")
    @Operation(
        summary = "Студент меняет email",
        description = "Пользователь отправляет новый email",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь сменил email",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорректный формат email",
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
    fun changeEmail(@RequestBody email: EmailDto) = studentService.changeEmail(getUsernameFromContext(), email)

    @PatchMapping("photo")
    @Operation(
        summary = "Студент меняет фото",
        description = "Пользователь отправляет сведения о недавно загруженном файле фото",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь сменил email",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорректный формат email",
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
    fun changePhoto(@RequestBody photoDto: PhotoDto): StudentDto =
        studentService.changePhoto(getUsernameFromContext(), photoDto.fileName)
}