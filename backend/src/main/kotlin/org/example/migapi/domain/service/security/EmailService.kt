package org.example.migapi.domain.service.security

import org.example.migapi.domain.model.entity.User
import org.springframework.mail.MailException

interface EmailService {
    @Throws(exceptionClasses = [MailException::class])
    fun sendRestoreEmail(user: User, token: String, url: String)

    @Throws(exceptionClasses = [MailException::class])
    fun sendTfaEmail(user: User, token: String)
}