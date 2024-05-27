package org.example.migapi.auth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    @Autowired
    private val mailSender: MailSender
) : EmailService {

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