package org.example.migapi.domain.account.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.account.dto.MainScreenDto
import org.example.migapi.domain.account.service.StudentService
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/main_screen")
@Tag(name = "Main Screen", description = "Controller displays main info on users first screen")
class MainScreenController(
    @Autowired
    private val studentService: StudentService,

    @Value("\${kemsu.logo}")
    private val logo: String
) {

    @GetMapping
    @Operation(
        summary = "Информация для главного экрана",
        description = "Пользователь получает данные для заполнения главного экрана",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Учетная запись найдена",
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
    fun mainInfo(): MainScreenDto {
        val student = studentService.getByUsername(getUsernameFromContext())

        return MainScreenDto(
            userId = student.id!!,
            logo = logo,
            name = student.name,
            surname = student.surname,
            patronymic = student.patronymic,
            photo = student.photo,
            institute = student.institute,
            group = student.group
        )
    }
}