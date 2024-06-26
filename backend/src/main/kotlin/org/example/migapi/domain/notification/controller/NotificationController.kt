package org.example.migapi.domain.notification.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.dto.NotificationDto
import org.example.migapi.domain.notification.model.Notification
import org.example.migapi.domain.notification.service.NotificationService
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/notifications")
@Tag(name = "Notification API", description = "Controller displays, delete, and mark as read user's notifications")
class NotificationController(
    @Autowired
    private val notificationService: NotificationService
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
    fun addToken(@RequestBody firebaseTokenDto: FirebaseTokenDto) =
        notificationService.addFirebaseToken(firebaseTokenDto)

    @PatchMapping
    @Operation(
        summary = "Клиент прочитал уведомление",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Уведомление прочитано",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Уведомление не найдено",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный формат id",
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
    fun viewNotification(@RequestBody notificationViewedDto: NotificationDto) {
        notificationService.changeNotificationViewed(
            notificationViewedDto.id ?: throw BadRequestException(),
            notificationViewedDto.isViewed ?: true
        )
    }

    @GetMapping
    @Operation(
        summary = "Клиент получает все уведомления",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Уведомления получены",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный формат id",
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
    fun getAllUserNotifications(): List<NotificationDto> =
        notificationService.findAllByUsername(getUsernameFromContext())

    @PostMapping
    @Operation(
        summary = "Отправка уведомления пользователю",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Уведомление отправлено",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный формат",
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
    fun pushNotification(@RequestBody notificationDto: NotificationDto): Notification =
        notificationService.pushNotification(notificationDto)

    @DeleteMapping
    @Operation(
        summary = "Удаление уведомления",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Уведомление удалено",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Уведомление не найдено",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный формат id",
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
    fun deleteNotification(@RequestBody notificationDto: NotificationDto) =
        notificationService.deleteNotification(notificationDto.id ?: throw BadRequestException())
}