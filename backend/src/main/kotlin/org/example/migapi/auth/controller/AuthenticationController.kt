package org.example.migapi.auth.controller

import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.auth.dto.*
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    @Autowired
    private val authenticationService: AuthenticationService
) {

    @PostMapping("signing")
    fun signIn(@RequestBody signRequest: SignRequest): SignResponse = authenticationService.authenticate(signRequest)

    @PostMapping("signing/tfa")
    fun verifySignIn(@RequestBody verificationRequest: VerificationRequest): SignResponse =
        authenticationService.verifyTfa(verificationRequest)

    @PostMapping("refresh")
    fun refresh(@RequestBody refreshTokenRequest: RefreshTokenRequest): SignResponse =
        authenticationService.refreshToken(refreshTokenRequest)

    @PostMapping("restore")
    fun block(@RequestBody email: String, request: HttpServletRequest) = authenticationService.blockUser4Restore(email, request)

    @PostMapping("restore/{token}")
    fun restore(@PathVariable token: String, @RequestBody passwords: Passwords) {
        if (passwords.password != passwords.confirmation)
            throw BadCredentialsException("Passwords are not equal")

        authenticationService.restoreUser(token, passwords)
    }
}