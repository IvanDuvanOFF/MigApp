package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.TfaCodeExpiredException
import org.example.migapi.auth.exception.VerificationTokenNotFoundException
import org.example.migapi.core.domain.model.entity.VerificationToken
import org.example.migapi.core.domain.repo.UserRepository
import org.example.migapi.core.domain.repo.VerificationTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class VerificationTokenService(
    @Autowired
    private val verificationTokenRepository: VerificationTokenRepository,
    @Autowired
    private val userRepository: UserRepository,
    @Value("\${mig.jwt.verification-expiration}")
    private val verificationTokenExpiration: Int
) {

    @Throws(exceptionClasses = [BadCredentialsException::class, PersistenceException::class])
    fun createVerificationToken(email: String): VerificationToken {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { BadCredentialsException("User with email $email not found") }

        return verificationTokenRepository.save(
            VerificationToken(
                expirationDate = LocalDateTime.now().plus(verificationTokenExpiration.toLong(), ChronoUnit.MILLIS),
                user = user
            )
        )
    }

    @Throws(
        exceptionClasses = [
            VerificationTokenNotFoundException::class,
            TfaCodeExpiredException::class,
            PersistenceException::class
        ]
    )
    fun deleteVerificationToken(token: String): VerificationToken {
        val verificationToken = verificationTokenRepository.findById(UUID.fromString(token))

        if (verificationToken.isEmpty)
            throw VerificationTokenNotFoundException("Verification token has not been found")
        if (verificationToken.get().expirationDate.isBefore(LocalDateTime.now()))
            throw TfaCodeExpiredException("Verification token has been expired")

        verificationTokenRepository.delete(verificationToken.get())

        return verificationToken.get()
    }
}