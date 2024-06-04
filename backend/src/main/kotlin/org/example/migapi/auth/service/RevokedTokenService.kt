package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.model.RevokedToken
import org.example.migapi.domain.account.model.User
import org.example.migapi.auth.repository.RevokedTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.DateTimeException
import java.time.ZoneId
import java.util.Date

/**
 * Сервис для работы с отозванными токенами доступа [RevokedToken]
 */
@Service
class RevokedTokenService(
    @Autowired
    private val revokedTokenRepository: RevokedTokenRepository
) {

    /**
     * Проверяет является ли токен отозванным
     *
     * @throws PersistenceException
     */
    fun isTokenRevoked(token: String): Boolean = revokedTokenRepository.findById(token).isPresent

    /**
     * Отзывает токен [token] для пользователя [user]
     *
     * @throws DateTimeException
     * @throws PersistenceException
     */
    fun revokeToken(token: String, user: User, expiredAt: Date) {
        val expired = expiredAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val revokedToken = RevokedToken(
            token = token,
            user = user,
            expirationDate = expired
        )

        revokedTokenRepository.save(revokedToken)
    }
}