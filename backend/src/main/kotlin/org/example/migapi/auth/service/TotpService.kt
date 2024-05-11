package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.TfaCodeExpiredException
import org.example.migapi.core.domain.model.entity.TfaCode
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.repo.TfaCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class TotpService(
    @Autowired
    private val tfaCodeRepository: TfaCodeRepository,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Value("\${mig.tfa.expiration-ms}")
    private val expiration: Int
) {
    private val random = SecureRandom()

    companion object {
        private const val SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        private const val CAPACITY = 6
    }

    @Throws(exceptionClasses = [DateTimeException::class, PersistenceException::class])
    fun generateCode(user: User): String {
        val code =
            StringBuilder(CAPACITY).apply { repeat(CAPACITY) { this.append(SOURCE[random.nextInt(SOURCE.length)]) } }
                .toString()

        val tfaCode = TfaCode(
            tfaId = TfaCode.TfaCodeId(passwordEncoder.encode(code), user),
            expirationDate = Instant
                .ofEpochMilli(System.currentTimeMillis() + expiration)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
        tfaCodeRepository.save(tfaCode)

        return code
    }

    @Throws(exceptionClasses = [PersistenceException::class, TfaCodeExpiredException::class])
    fun validateCode(user: User, code: String): Boolean =
        tfaCodeRepository.findTfaCodesByTfaIdUser(user).firstOrNull { passwordEncoder.matches(code, it.tfaId.code) }
            ?.let {
                if (it.expirationDate < LocalDateTime.now())
                    throw TfaCodeExpiredException("Code is expired")

                tfaCodeRepository.delete(it)

                true
            } ?: false
}