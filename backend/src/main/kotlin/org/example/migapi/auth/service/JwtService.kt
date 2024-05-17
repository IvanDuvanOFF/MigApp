package org.example.migapi.auth.service

import io.jsonwebtoken.JwtException
import org.example.migapi.core.domain.model.entity.User
import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun generateToken(user: User, extraClaims: Map<String, Any>): String

    fun generateRefreshToken(user: User, extraClaims: Map<String, Any>): String

    @Throws(exceptionClasses = [JwtException::class])
    fun extractUsername(token: String): String

    @Throws(exceptionClasses = [JwtException::class, ClassCastException::class])
    fun extractExtraClaim(token: String, claimName: String): String

    fun isTokenValid(token: String, user: User): Boolean

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}