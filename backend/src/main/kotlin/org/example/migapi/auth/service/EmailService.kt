package org.example.migapi.auth.service

import org.springframework.mail.MailException

interface EmailService {
    fun validate(email: String): Boolean

    @Throws(exceptionClasses = [MailException::class])
    fun sendRestoreEmail(userEmail: String, token: String, url: String)

    @Throws(exceptionClasses = [MailException::class])
    fun sendTfaEmail(userEmail: String, token: String)
}