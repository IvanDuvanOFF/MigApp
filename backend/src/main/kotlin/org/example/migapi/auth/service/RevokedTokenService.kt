package org.example.migapi.auth.service

import org.example.migapi.auth.model.RevokedToken
import org.example.migapi.domain.account.model.User
import org.example.migapi.auth.repository.RevokedTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.Date

@Service
class RevokedTokenService(
    @Autowired
    private val revokedTokenRepository: RevokedTokenRepository
) {
    fun isTokenRevoked(token: String): Boolean = revokedTokenRepository.findById(token).isPresent

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