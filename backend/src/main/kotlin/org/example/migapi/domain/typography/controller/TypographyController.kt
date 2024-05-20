package org.example.migapi.domain.typography.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.typography.dto.DocumentDto
import org.example.migapi.domain.typography.dto.TypographyDto
import org.example.migapi.domain.typography.model.DocumentType
import org.example.migapi.domain.typography.service.DocumentService
import org.example.migapi.domain.typography.service.TypographyService
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/applications")
class TypographyController(
    @Autowired
    private val typographyService: TypographyService,
    @Autowired
    private val documentService: DocumentService
) {

    @GetMapping("/all/{filter_date}")
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
    fun allApplications(@PathVariable(required = false, name = "filter_date") filterDate: String? = null) =
        typographyService.findAllTitlesByUsername(getUsernameFromContext(), filterDate)

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

    @GetMapping("/{application_id}/list/types")
    @Operation(
        summary = "Получение списка типов документов для данного оформления",
        description = "Пользователь получает офрмление по id",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список получен",
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
    fun applicationList(@PathVariable(name = "application_id") applicationId: String): List<DocumentType> =
        typographyService.getTypographyListById(applicationId)

    @PostMapping("/{application_id}")
    @Operation(
        summary = "Добавление документа к оформлению",
        description = "Пользователь добавляет документ к оформлению по id",
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
    fun addDocument(@PathVariable(name = "application_id") applicationId: String, @RequestBody document: DocumentDto) =
        documentService.saveDocument(applicationId, document)

    @GetMapping("/{application_id}/{document_id}")
    @Operation(
        summary = "Получение документа из оформления",
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
    fun getDocument(
        @PathVariable(name = "application_id") applicationId: String,
        @PathVariable(name = "document_id") documentId: String
    ): DocumentDto = documentService.getById(documentId)
}