package org.example.migapi.auth.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.*
import org.springframework.stereotype.Service

/**
 * Сервис для работы с email-ами
 */
@Service
class EmailServiceImpl(
    @Autowired
    private val mailSender: MailSender
) : EmailService {

    /**
     * Отправляет сообщение
     */
    override fun sendRestoreEmail(userEmail: String, token: String, url: String) {
        val email = SimpleMailMessage()

        email.setTo(userEmail)
        email.subject = "Restoring password"
        email.text = "$url/auth/restore/$token"

        mailSender.send(email)
    }

    /**
     * Отправляет сообщение с кодом 2х факторной аутентификации [token] пользователю на адрес [userEmail]
     *
     * @throws MailParseException сообщение не получилось отправить
     * @throws MailAuthenticationException сообщение не получилось отправить
     * @throws MailSendException сообщение не получилось отправить
     */
    override fun sendTfaEmail(userEmail: String, token: String) {
        val email = SimpleMailMessage()

        email.setTo(userEmail)
        email.subject = "Tfa code"
        email.text = token

        mailSender.send(email)
    }
}