package org.example.migapi.domain.service.security

import org.example.migapi.domain.model.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    @Autowired
    private val mailSender: MailSender
) : EmailService {

    override fun sendRestoreEmail(user: User, token: String, url: String) {
        val email = SimpleMailMessage()

        email.setTo(user.email)
        email.subject = "Restoring password"
        email.text = "$url/auth/restore/$token"

        mailSender.send(email)
    }

    override fun sendTfaEmail(user: User, token: String) {
        val email = SimpleMailMessage()

        email.setTo(user.email)
        email.subject = "Tfa code"
        email.text = token

        mailSender.send(email)
    }
}