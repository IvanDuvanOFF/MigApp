package org.example.migapi.controller.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.domain.dto.auth.*
import org.example.migapi.domain.service.data.UserService
import org.example.migapi.exception.BadCredentialsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    @Autowired
    private val userService: UserService
) {

    @PostMapping("signing")
    fun signIn(@RequestBody signRequest: SignRequest): SignResponse = userService.signIn(signRequest)

    @PostMapping("signing/tfa")
    fun verifySignIn(@RequestBody verificationRequest: VerificationRequest): SignResponse =
        userService.verifyCode(verificationRequest)

    @PostMapping("refresh")
    fun refresh(@RequestBody refreshTokenRequest: RefreshTokenRequest): SignResponse =
        userService.refreshToken(refreshTokenRequest)

    @PostMapping("restore")
    fun block(@RequestBody email: String, request: HttpServletRequest) = userService.blockUser4Restore(email, request)

    @PostMapping("restore/{token}")
    fun restore(@PathVariable token: String, @RequestBody passwords: Passwords) {
        if (passwords.password != passwords.confirmation)
            throw BadCredentialsException("Passwords are not equal")

        userService.restoreUser(token, passwords.password)
    }
}