package com.srthk.quickstart.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JWTService {
    private val secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
    fun extractUserEmail(jwtToken: String): String? {
        return extractClaim(token = jwtToken, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }


    private val signingKey: SecretKey
        get() {
            val keyBytes = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }


    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).payload
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUserEmail(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun generateToken(
        userDetails: UserDetails
    ): String {
        return generateToken(
            HashMap(), userDetails = userDetails
        )
    }

    fun generateToken(
        extraClaims: Map<String, Any>, userDetails: UserDetails
    ): String {
        return Jwts.builder().claims(extraClaims).subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis())).expiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(signingKey).compact()

    }
}
