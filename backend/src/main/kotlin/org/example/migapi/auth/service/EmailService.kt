package org.example.migapi.auth.service

interface EmailService {

    fun sendRestoreEmail(userEmail: String, token: String, url: String)

    fun sendTfaEmail(userEmail: String, token: String)
}