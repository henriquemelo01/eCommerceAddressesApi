package com.example.addressesecommerceservice.services.jwt

import com.example.addressesecommerceservice.services.users.UserService
import com.example.addressesecommerceservice.services.commons.erros.EnvPropertyNotFoundException
import com.example.addressesecommerceservice.services.jwt.erros.ClaimNotFoundException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class JwtService(environment: Environment) {

    private val jwtSecretKey = environment.getProperty(JWT_SECRET_ENV_PROPERTY)?.let {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(it))
    }

    fun extractUsernameFromJwt(
            jwt: String
    ) = extractClaim(jwt, Claims::getSubject) ?: throw ClaimNotFoundException("sub")

    fun generateJwt(
            userDetails: UserDetails,
            extraClaims: HashMap<String, Any> = hashMapOf()
    ) = Jwts
            .builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .addClaims(extraClaims)
            .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
            .compact()

    fun isTokenValid(jwt: String, userDetails: UserDetails): Boolean {
        val isUserValid = userDetails.username == extractUsernameFromJwt(jwt)

        val expirationDate = extractClaim(jwt, Claims::getExpiration) ?: throw ClaimNotFoundException("exp")
        val isDateExpired = expirationDate.before(Date())

        return isUserValid && !isDateExpired
    }

    fun extractAllClaims(jwt: String): Claims {
        val jwtSecretKey = jwtSecretKey ?: throw EnvPropertyNotFoundException(JWT_SECRET_ENV_PROPERTY)
        return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(jwt).body
    }

    fun <T> extractClaim(jwt: String, claimResolver: (Claims) -> T): T {
        val claims = extractAllClaims(jwt)
        return claimResolver(claims)
    }

    private companion object {
        const val JWT_SECRET_ENV_PROPERTY = "jwt.secret.key"
    }
}