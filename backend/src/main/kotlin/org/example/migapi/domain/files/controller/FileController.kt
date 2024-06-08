package org.example.migapi.domain.files.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.migapi.domain.files.model.File
import org.example.migapi.domain.files.service.FileService
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
@Tag(name = "File", description = "Controller allows upload and download both private and public files")
class FileController(
    @Autowired
    private val fileService: FileService
) {

    @PostMapping("upload")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Пользователь загружает файл",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь успешно загрузил файл",
                content = [Content(schema = Schema(implementation = File::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Токен просрочен или заблокирован",
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
    fun uploadUserFile(@RequestParam("file") file: MultipartFile): File =
        fileService.storePrivateFile(file, getUsernameFromContext())

    @GetMapping("/{filename}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Пользователь скачивает файл по его имени",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь скачал файл"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Файл не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Токен просрочен или заблокирован",
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
    fun getUserFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileService.loadPrivateFile(filename, getUsernameFromContext())

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.filename}\"")
            .body(file)
    }

    @DeleteMapping("/{filename}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Пользователь удаляет файл по его имени",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь удалил файл"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Файл не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Токен просрочен или заблокирован",
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
    fun removeUserFile(@PathVariable filename: String) {
        fileService.deletePrivateFile(filename, getUsernameFromContext())
    }

    @GetMapping("public/{filename}")
    @Operation(
        summary = "Пользователь скачивает публичный файл по его имени",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь скачал файл"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Файл не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun getPublicFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileService.loadPublicFile(filename)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.filename}\"")
            .body(file)
    }
}