package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.model.TotpCode
import org.example.migapi.domain.account.model.User
import org.example.migapi.auth.repository.TotpCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneId

/**
 * Сервис для работы с одноразовыми паролями [TotpCode]
 */
@Component
class TotpService(
    @Autowired
    private val totpCodeRepository: TotpCodeRepository,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Value("\${mig.tfa.expiration-ms}")
    private val expiration: Int
) {
    private val random = SecureRandom()

    companion object {
        private const val SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val CAPACITY = 6
    }

    /**
     * Метод генерирует одноразовый пароль [TotpCode] из символов в [SOURCE]
     * для пользователя [user]
     *
     * @throws DateTimeException
     * @throws PersistenceException
     */
    fun generateCode(user: User): String {
        val code =
            StringBuilder(CAPACITY).apply { repeat(CAPACITY) { this.append(SOURCE[random.nextInt(SOURCE.length)]) } }
                .toString()

        val totpCode = TotpCode(
            tfaId = TotpCode.TotpCodeId(passwordEncoder.encode(code), user),
            expirationDate = Instant
                .ofEpochMilli(System.currentTimeMillis() + expiration)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
        totpCodeRepository.save(totpCode)

        return code
    }

    /**
     * Метод находит одноразовый пароль [TotpCode] для пользователя [user]
     *
     * @throws BadCredentialsException код не найден
     */
    fun findTfaByUser(user: User, code: String): TotpCode {
        val codes =
            totpCodeRepository.findByTfaIdUser(user.id).orElseThrow { BadCredentialsException("Code is incorrrrect") }

        return codes.takeIf { passwordEncoder.matches(code, it.tfaId.code) }
            ?: throw BadCredentialsException("aboba")
    }

    /**
     * Удаляет одноразовый пароль [totpCode]
     *
     * @throws PersistenceException
     */
    fun removeCode(totpCode: TotpCode) = totpCodeRepository.delete(totpCode)
}