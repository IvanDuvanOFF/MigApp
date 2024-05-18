package org.example.migapi.core.config.iof.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.migapi.core.config.iof.model.File
import org.example.migapi.core.config.iof.service.FileService
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.core.domain.model.SpringUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
@RequestMapping("/api/files")
class FileController(
    @Autowired
    private val fileService: FileService
) {

    @PostMapping("upload")
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
    fun uploadFile(@RequestParam("file") file: MultipartFile): File = fileService.storeFile(file, getUsername())

    @GetMapping("/{filename}")
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
    fun getFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileService.load(filename, getUsername())

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.filename}\"")
            .body(file)
    }

    @DeleteMapping("/{filename}")
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
    fun removeFile(@PathVariable filename: String) {
        fileService.delete(filename, getUsername())
    }

    fun getUsername(): String = (SecurityContextHolder.getContext().authentication.principal as SpringUser).username
}