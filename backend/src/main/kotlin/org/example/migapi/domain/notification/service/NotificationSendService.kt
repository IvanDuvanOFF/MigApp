package org.example.migapi.domain.notification.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.PersistenceException
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.model.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Сервис для отправки уведомлений
 */
@Service
class NotificationSendService(
    @Autowired
    private val firebaseMessaging: FirebaseMessaging,
    @Autowired
    private val firebaseTokenService: FirebaseTokenService,
) {

    private val logger = KotlinLogging.logger { }

    /**
     * Метод для добавления токена Firebase [firebaseTokenDto] в систему
     *
     * @throws UserNotFoundException пользователь не найден
     * @throws IllegalArgumentException id пользователя некорректного формата
     * @throws PersistenceException
     */
    fun addFirebaseToken(firebaseTokenDto: FirebaseTokenDto) = firebaseTokenService.save(firebaseTokenDto)

    /**
     * Отправляет уведомление [notification] пользователю на все его устройства при помощи токенов Firebase
     *
     * @return true если отправка прошла успешно, false - если произошла ошибка
     */
    fun sendNotification(notification: Notification): Boolean = try {
        val firebaseTokens = firebaseTokenService.findAllFirebaseTokensByUsername(notification.user)
        firebaseTokens.forEach {
            try {
                firebaseMessaging.send(notification.message(it))
            } catch (e: FirebaseMessagingException) {
                logger.error { e.message }
                logger.error { e.stackTrace }

                return false
            }
        }

        true
    } catch (e: PersistenceException) {
        logger.error { e.message }
        logger.error { e.stackTrace }

        false
    }
}