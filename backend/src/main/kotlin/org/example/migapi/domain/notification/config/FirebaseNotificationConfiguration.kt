package org.example.migapi.domain.notification.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import java.io.IOException

@Configuration
class FirebaseNotificationConfiguration(
    @Value("\${firebase.key}")
    private val keyPath: String
) {

    @Bean
    @Throws(exceptionClasses = [IOException::class])
    fun firebaseMessaging(): FirebaseMessaging {
        val credentials = GoogleCredentials
            .fromStream(ClassPathResource(keyPath).inputStream)

        val firebaseOptions = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()

        val firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "mig-app")

        return FirebaseMessaging.getInstance(firebaseApp)
    }
}

typealias FirebaseNotification = Notification