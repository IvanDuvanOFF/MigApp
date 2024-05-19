package org.example.migapi.domain.typography.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.typography.dto.TypographyDto
import org.example.migapi.domain.typography.dto.TypographyTitleDto
import org.example.migapi.domain.typography.service.TypographyService
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/applications")
class TypographyController(
    @Autowired
    private val typographyService: TypographyService
) {

    @GetMapping
    @Operation(
        summary = "Список всех оформлений кратко",
        description = "Пользователь получает список всех оформлений кратко",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список получен",
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
    fun allApplications(): List<TypographyTitleDto> =
        typographyService.findAllTitlesByUsername(getUsernameFromContext())

    @GetMapping("/{application_id}")
    @Operation(
        summary = "Получение конкретного оформления",
        description = "Пользователь получает офрмление по id",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Оформление получено",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Оформление не найдено",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорректный формат id",
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
    fun applicationById(@PathVariable(name = "application_id") applicationId: String): TypographyDto =
        typographyService.findById(applicationId)
}