package org.example.migapi.core.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.migapi.auth.service.JwtService
import org.example.migapi.auth.service.MigUserDetailsService
import org.example.migapi.auth.service.RevokedTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Компонент, фильтрующий запросы
 */
@Component
class JwtAuthenticationFilter(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val homeUserDetailsService: MigUserDetailsService,
    @Autowired
    private val revokedTokenService: RevokedTokenService
) : OncePerRequestFilter() {

    /**
     * Фильтрует запросы, валидирует jwt-токен,
     * если контекст пустой, авторизует пользователя
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)

        if (revokedTokenService.isTokenRevoked(jwt)) {
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtService.extractUsername(jwt)

        if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = homeUserDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(jwt, userDetails)) {
                val securityContext = SecurityContextHolder.createEmptyContext()

                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)
            }
        }

        filterChain.doFilter(request, response)
    }

}