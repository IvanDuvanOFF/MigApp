package org.example.migapi.domain.notification.service

import org.example.migapi.domain.account.service.UserService
import org.example.migapi.domain.notification.dto.FirebaseTokenDto
import org.example.migapi.domain.notification.model.FirebaseToken
import org.example.migapi.domain.notification.repository.FirebaseTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseTokenService(
    @Autowired
    private val firebaseTokenRepository: FirebaseTokenRepository,
    @Autowired
    private val userService: UserService
) {
    fun save(firebaseTokenDto: FirebaseTokenDto) {
        val firebaseToken = FirebaseToken(
            firebaseTokenDto.token,
            user = userService.findById(firebaseTokenDto.userId)
        )

        firebaseTokenRepository.save(firebaseToken)
    }
}