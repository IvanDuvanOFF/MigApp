package org.example.migapi.domain.notification.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.service.FirebaseTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/notification")
class NotificationController(
    @Autowired
    private val firebaseTokenService: FirebaseTokenService
) {

    @PutMapping
    @Operation(
        summary = "Клиент добавляет свой токен Firebase в систему",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Токен добавлен",
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
    fun addToken(firebaseTokenDto: FirebaseTokenDto) = firebaseTokenService.save(firebaseTokenDto)
}