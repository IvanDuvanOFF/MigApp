package org.example.migapi.auth.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.domain.account.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MigUserDetailsService(
    @Autowired
    private val userService: UserService
) : UserDetailsService {

    @Throws(exceptionClasses = [BadCredentialsException::class, PersistenceException::class])
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrEmpty())
            throw BadCredentialsException("Username cannot be null")

        val user = userService.findUserByUsernameOrEmail(username)

        return user.toSpringUser()
    }
}