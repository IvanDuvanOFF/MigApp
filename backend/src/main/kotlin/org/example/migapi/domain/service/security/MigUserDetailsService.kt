package org.example.migapi.domain.service.security

import org.example.migapi.exception.UserNotFoundException
import org.example.migapi.exception.UsernameNullException
import org.example.migapi.repository.UserRepository
import org.example.migapi.utils.EmailValidator
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
    private val emailValidator: EmailValidator
) : UserDetailsService {

    @Throws(exceptionClasses = [UsernameNullException::class, UserNotFoundException::class])
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null)
            throw UsernameNullException("Username cannot be null")

        val user = (if (emailValidator.validate(username)) userRepository.findUserByEmail(username)
        else userRepository.findUserByUsername(username))
            .orElseThrow { UserNotFoundException("Invalid username or email") }

        return user.toSpringUser()
    }
}