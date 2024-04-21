package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.core.domain.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class MigUserDetailsService(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val emailService: EmailService
) : UserDetailsService {

    @Throws(exceptionClasses = [BadCredentialsException::class, PersistenceException::class])
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null)
            throw BadCredentialsException("Username cannot be null")

        val user = (if (emailService.validate(username)) userRepository.findUserByEmail(username)
        else userRepository.findUserByUsername(username))
            .orElseThrow { BadCredentialsException("Invalid username or email") }

        return user.toSpringUser()
    }
}