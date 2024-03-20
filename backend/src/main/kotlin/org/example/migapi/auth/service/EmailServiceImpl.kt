package org.example.migapi.auth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class EmailServiceImpl(
    @Autowired
    private val mailSender: MailSender
) : EmailService {

    private val emailPattern =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"

    private val pattern = Pattern.compile(emailPattern)

    override fun validate(email: String) = pattern.matcher(email).matches()

    override fun sendRestoreEmail(userEmail: String, token: String, url: String) {
        val email = SimpleMailMessage()

        email.setTo(userEmail)
        email.subject = "Restoring password"
        email.text = "$url/auth/restore/$token"

        mailSender.send(email)
    }

    override fun sendTfaEmail(userEmail: String, token: String) {
        val email = SimpleMailMessage()

        email.setTo(userEmail)
        email.subject = "Tfa code"
        email.text = token

        mailSender.send(email)
    }
}