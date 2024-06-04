package org.example.migapi.domain.notification.service

import jakarta.persistence.PersistenceException
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.account.service.UserService
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.model.FirebaseToken
import org.example.migapi.domain.notification.repository.FirebaseTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Сервис для работы с токенами Firebase
 */
@Service
class FirebaseTokenService(
    @Autowired
    private val firebaseTokenRepository: FirebaseTokenRepository,
    @Autowired
    private val userService: UserService
) {

    /**
     * Метод сохраняет токен Firebase [firebaseTokenDto]
     *
     * @throws UserNotFoundException пользователь не найден
     * @throws IllegalArgumentException id пользователя некорректного формата
     * @throws PersistenceException
     */
    fun save(firebaseTokenDto: FirebaseTokenDto) {
        val firebaseToken = FirebaseToken(
            firebaseTokenDto.token,
            user = userService.findById(firebaseTokenDto.userId)
        )

        firebaseTokenRepository.save(firebaseToken)
    }

    /**
     * Метод находит все токены Firebase [List]<[FirebaseToken]> для пользователя [user]
     *
     * @throws PersistenceException
     */
    fun findAllFirebaseTokensByUsername(user: User): List<FirebaseToken> =
        firebaseTokenRepository.findAllByUser(user)
}