package org.example.migapi.auth.service

import io.jsonwebtoken.JwtException
import org.example.migapi.core.domain.model.entity.User
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface JwtService {
    fun generateToken(user: User, extraClaims: Map<String, Any> = emptyMap()): String

    fun generateRefreshToken(user: User, extraClaims: Map<String, Any> = emptyMap()): String

    @Throws(exceptionClasses = [JwtException::class])
    fun extractUsername(token: String): String

    @Throws(exceptionClasses = [JwtException::class, ClassCastException::class])
    fun extractExtraClaim(token: String, claimName: String): String

    fun extractExpirationDate(token: String): Date

    fun isTokenValid(token: String, user: User): Boolean

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}