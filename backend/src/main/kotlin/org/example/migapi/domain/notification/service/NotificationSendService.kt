package org.example.migapi.domain.notification.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.example.migapi.domain.notification.model.FirebaseToken
import org.example.migapi.domain.notification.model.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationSendService(
    @Autowired
    private val firebaseMessaging: FirebaseMessaging,
) {

    private val logger = KotlinLogging.logger { }

    fun sendNotification(notification: Notification, firebaseToken: FirebaseToken): Boolean = try {
        firebaseMessaging.send(notification.message(firebaseToken))
        true
    } catch (e: FirebaseMessagingException) {
        logger.error { e.message }
        logger.error { e.stackTrace }

        false
    }
}