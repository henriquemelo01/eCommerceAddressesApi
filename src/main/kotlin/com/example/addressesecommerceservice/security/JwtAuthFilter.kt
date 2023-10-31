package com.example.addressesecommerceservice.security

import com.example.addressesecommerceservice.services.jwt.JwtService
import com.example.addressesecommerceservice.services.users.UserService
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthFilter(
        private val jwtService: JwtService,
        private val userService: UserService,
        private val genericApplicationContext: GenericApplicationContext
) : OncePerRequestFilter() {

    private val securityContext by lazy {
        SecurityContextHolder.getContext()
    }

    private val jwtAuthorizationHeaderProvider
        get(): JwtAuthorizationHeaderProvider? = try {
            genericApplicationContext.getBean(JwtAuthorizationHeaderProvider::class.java)
        } catch (e: Exception) {
            null
        }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)

        if (authorizationHeader.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authorizationHeader.removePrefix(BEARER).trim()

        val username = jwtService.extractUsernameFromJwt(jwt)
        val user = userService.loadUserByUsername(username)
        val isUserAlreadyConnected = securityContext.authentication != null

        if (jwtService.isTokenValid(jwt = jwt, userDetails = user) && !isUserAlreadyConnected) {

            jwtAuthorizationHeaderProvider
                    ?.let { it.value = jwt }
                    ?: genericApplicationContext.registerBean {
                        JwtAuthorizationHeaderProvider(jwt)
                    }

            val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }
            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER = "Bearer"
    }
}