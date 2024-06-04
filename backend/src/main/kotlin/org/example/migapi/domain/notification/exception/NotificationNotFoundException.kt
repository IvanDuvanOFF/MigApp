package org.example.migapi.domain.notification.exception

import org.example.migapi.core.config.exception.MigApplicationException
import org.springframework.http.HttpStatus

/**
 * @property httpStatus 404
 */
class NotificationNotFoundException(message: String = "Notification not found") :
    MigApplicationException(HttpStatus.NOT_FOUND, message)