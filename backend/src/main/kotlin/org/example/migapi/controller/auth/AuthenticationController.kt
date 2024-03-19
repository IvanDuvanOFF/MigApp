package org.example.migapi.controller.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.migapi.domain.dto.auth.RefreshTokenRequest
import org.example.migapi.domain.dto.auth.SignRequest
import org.example.migapi.domain.dto.auth.SignResponse
import org.example.migapi.domain.dto.util.Redirect
import org.example.migapi.domain.service.data.UserService
import org.example.migapi.exception.PasswordsAreNotEqualException
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val mailSender: MailSender,
    @Autowired
    private val migUtils: MigUtils
) {

    @PostMapping("signin")
    fun signIn(@RequestBody signRequest: SignRequest): SignResponse = userService.signIn(signRequest)

    @PostMapping("refresh")
    fun refresh(@RequestBody refreshTokenRequest: RefreshTokenRequest): SignResponse =
        userService.refreshToken(refreshTokenRequest)

    @PostMapping("restore")
    fun block(@RequestBody email: String?, request: HttpServletRequest): Redirect {
        val user = userService.blockUser(email)

        val url = migUtils.getHostUrl(request)
        val message = userService.prepareEmail(user, url)
        mailSender.send(message)

        return Redirect(url)
    }

    inner class Passwords(val password: String, val confirmation: String)

    @PostMapping("restore/{token}")
    fun restore(@PathVariable token: String, @RequestBody passwords: Passwords) {
        if (passwords.password != passwords.confirmation)
            throw PasswordsAreNotEqualException()

        userService.restoreUser(token, passwords.password)
    }
}