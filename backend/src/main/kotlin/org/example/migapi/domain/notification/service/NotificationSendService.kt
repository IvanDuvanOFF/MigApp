package org.example.migapi.domain.notification.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.model.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationSendService(
    @Autowired
    private val firebaseMessaging: FirebaseMessaging,
    @Autowired
    private val firebaseTokenService: FirebaseTokenService,
) {

    private val logger = KotlinLogging.logger { }

    fun addFirebaseToken(firebaseTokenDto: FirebaseTokenDto) = firebaseTokenService.save(firebaseTokenDto)

    fun sendNotification(notification: Notification): Boolean = try {
        val firebaseTokens = firebaseTokenService.findAllFirebaseTokensByUsername(notification.user)
        firebaseTokens.forEach { firebaseMessaging.send(notification.message(it)) }

        true
    } catch (e: FirebaseMessagingException) {
        logger.error { e.message }
        logger.error { e.stackTrace }

        false
    }
}