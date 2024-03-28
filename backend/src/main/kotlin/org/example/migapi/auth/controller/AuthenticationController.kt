package org.example.migapi.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.service.AuthenticationService
import org.example.migapi.core.domain.dto.Error
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    @Autowired
    private val authenticationService: AuthenticationService
) {

    @PostMapping("signing")
    @Operation(
        summary = "Авторизация пользователя",
        description = "Пользователь вводит логин и пароль и получает JWT-токен. " +
                "Если у пользователя включена 2х факторная аутентификация, приходит соответсвующий ответ",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешная авторизация",
                content = [Content(schema = Schema(implementation = SignResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный пароль",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "423",
                description = "Пользователь заблокирован",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun signIn(@RequestBody signRequest: SignRequest): SignResponse = authenticationService.authenticate(signRequest)

    @PostMapping("signing/tfa")
    @Operation(
        summary = "Проверка кода 2х факторной аутентификации",
        description = "Пользователь вводит код, полученный на электронную почту",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Код подтвержден корректный, вход одобрен",
                content = [Content(schema = Schema(implementation = SignResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный код",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "2х факторная аутентификация не включена",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun verifySignIn(@RequestBody verificationRequest: VerificationRequest): SignResponse =
        authenticationService.verifyTfa(verificationRequest)

    @PostMapping("refresh")
    @Operation(
        summary = "Обновление JWT-токена",
        description = "Клиент отправляет запрос, когда у токкена выходит срок годности",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "JWT-токен обновлен",
                content = [Content(schema = Schema(implementation = SignResponse::class))]
            ),
            ApiResponse(
                responseCode = "410",
                description = "Токен обновления просрочен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Хозяин токена не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Токен восстановления невалиден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun refresh(@RequestBody refreshTokenRequest: RefreshTokenRequest): SignResponse =
        authenticationService.refreshToken(refreshTokenRequest)

    @PostMapping("restore")
    @Operation(
        summary = "Запрос на восстановление пароля",
        description = "Пользователь вводит email и ждет получения ссылки для восстановления пароля",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь заблокирован для восстановления пароля",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверный email",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun block(@RequestBody email: String, request: HttpServletRequest) =
        authenticationService.blockUser4Restore(email, request)

    @PostMapping("restore/{token}")
    @Operation(
        summary = "Пользователь восстанавливает доступ",
        description = "Пользователь вводит новый пароль и восстанавливает доступ к аккаунту",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Пользователь восстановил доступ",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Пароли не совпадают",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Пользователь уже восстановлен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Токен восстановления не найден",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "410",
                description = "Токен восстановления просрочен",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun restore(@PathVariable token: String, @RequestBody passwords: Passwords) =
        authenticationService.restoreUser(token, passwords)
}