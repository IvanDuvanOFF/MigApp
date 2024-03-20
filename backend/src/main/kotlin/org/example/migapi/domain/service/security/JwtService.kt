package org.example.migapi.domain.service.security

import io.jsonwebtoken.JwtException
import org.example.migapi.domain.model.entity.User
import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun generateToken(user: User): String

    fun generateRefreshToken(extraClaims: Map<String, Any>, user: User): String

    @Throws(exceptionClasses = [JwtException::class])
    fun extractUsername(token: String): String

    fun isTokenValid(token: String, user: User): Boolean

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}