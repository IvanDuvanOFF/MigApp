package org.example.migapi.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.example.migapi.domain.account.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

/**
 * Сервис для работы с jwt-токенами
 */
@Service
@Scope("prototype")
class JwtServiceImpl(
    @Value("\${mig.jwt.secret}")
    private val jwtSecret: String,
    @Value("\${mig.jwt.expiration-ms}")
    private val jwtExpirationMs: Long,
    @Value("\${mig.jwt.refresh-expiration}")
    private val refreshExpiration: Long
) : JwtService {

    /**
     * Генерирует jwt-токен [String] для пользователя [user]
     */
    override fun generateToken(user: User, extraClaims: Map<String, Any>): String {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Генерирует refresh-токен [String] для пользователя [user]
     */
    override fun generateRefreshToken(user: User, extraClaims: Map<String, Any>): String = Jwts
        .builder().setClaims(extraClaims).setSubject(user.username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + refreshExpiration))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact()

    /**
     * Достает имя пользователя [String] из токена [token]
     *
     * @throws JwtException
     */
    override fun extractUsername(token: String): String = extractAllClaims(token).subject

    /**
     * Достает дополнительные клэимы из токена [token] по имени [claimName]
     *
     * @throws JwtException
     * @throws ClassCastException
     */
    override fun extractExtraClaim(token: String, claimName: String): String =
        extractAllClaims(token)[claimName] as String

    /**
     * Проверяет валидность токена [token] для пользователя [user]
     */
    override fun isTokenValid(token: String, user: User): Boolean {
        val username = extractUsername(token)

        return (username == user.username && !isTokenExpired(token))
    }

    /**
     * Проверяет валидность токена [token] для пользователя [userDetails]
     */
    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)

        return (username == userDetails.username && !isTokenExpired(token))
    }

    /**
     * Достает срок годности токена [Date] из токена [token]
     *
     * @throws JwtException
     */
    override fun extractExpirationDate(token: String): Date = extractClaim(token, Claims::getExpiration)

    /**
     * Проверяет не испорчен ли токен [token]
     */
    private fun isTokenExpired(token: String): Boolean = extractClaim(token, Claims::getExpiration).before(Date())

    /**
     * Достает дополнительные клэимы из токена [token]
     */
    private fun <T> extractClaim(token: String, claimsResolver: (claims: Claims) -> T): T =
        claimsResolver(extractAllClaims(token))

    /**
     * Достает подписанта из токена
     */
    private fun getSignKey(): Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    /**
     * Достает все клэимы
     *
     * @throws JwtException
     * @throws ClassCastException
     */
    private fun extractAllClaims(token: String): Claims = Jwts
        .parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .body
}