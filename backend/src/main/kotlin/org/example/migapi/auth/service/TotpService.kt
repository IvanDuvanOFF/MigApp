package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.core.domain.model.entity.TotpCode
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.repo.TotpCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneId

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
        private const val SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        private const val CAPACITY = 6
    }

    @Throws(exceptionClasses = [DateTimeException::class, PersistenceException::class])
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

    @Throws(exceptionClasses = [BadCredentialsException::class])
    fun findTfaByUser(user: User, code: String): TotpCode {
        val codes =
            totpCodeRepository.findAllByTfaIdUser(user).orElseThrow { BadCredentialsException("Code is incorrrrect") }
                .firstOrNull { passwordEncoder.matches(code, it.tfaId.code) }
                ?: throw BadCredentialsException("aboba")

        return codes
    }

    fun removeCode(totpCode: TotpCode) = totpCodeRepository.delete(totpCode)
}