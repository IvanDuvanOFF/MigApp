package org.example.migapi.domain.service.security

import jakarta.persistence.PersistenceException
import org.example.migapi.domain.model.entity.TfaCode
import org.example.migapi.domain.model.entity.User
import org.example.migapi.repository.TfaCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${mig.tfa.expiration-ms}")
    private val expiration: Int
) {
    private val random = SecureRandom()

    companion object {
        private const val SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        private const val CAPACITY = 6
    }

    @Throws(exceptionClasses = [DateTimeException::class, PersistenceException::class])
    fun generateCode(user: User): TfaCode {
        val code =
            StringBuilder(CAPACITY).apply { repeat(CAPACITY) { this.append(SOURCE[random.nextInt(SOURCE.length)]) } }
                .toString()

        val tfaCode = TfaCode(
            tfaId = TfaCode.TfaCodeId(code, user),
            expirationDate = Instant
                .ofEpochMilli(System.currentTimeMillis() + expiration)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )

        return tfaCodeRepository.save(tfaCode)
    }

    @Throws(exceptionClasses = [PersistenceException::class])
    fun validateCode(user: User, code: String): Boolean {
        val tfaCode = tfaCodeRepository.findTfaCodeByTfaId(TfaCode.TfaCodeId(code, user))

        return !(tfaCode.isEmpty || LocalDateTime.now() > tfaCode.get().expirationDate)
    }
}