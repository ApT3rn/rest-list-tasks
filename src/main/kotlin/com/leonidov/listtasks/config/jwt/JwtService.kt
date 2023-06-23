package com.leonidov.listtasks.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(@Value("\${jwt.secret.access}") private val jwtAccessSecret: String,
                 @Value("\${jwt.secret.refresh}") private val jwtRefreshSecret: String) {

    fun extractUsernameFromRefreshToken(token: String): String = extractClaim(token, Claims::getSubject, jwtRefreshSecret)

    fun extractUsernameFromAccessToken(token: String): String = extractClaim(token, Claims::getSubject, jwtAccessSecret)

    fun <T> extractClaim(token: String, claimsResolver: java.util.function.Function<Claims, T>, jwtSecretToken: String) : T {
        val claims: Claims = extractAllClaims(token, jwtSecretToken)
        return claimsResolver.apply(claims)
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        val now: LocalDateTime = LocalDateTime.now()
        val accessExpirationInstant: Instant = now.plusMinutes(5L)
            .atZone(ZoneId.systemDefault()).toInstant()
        return Jwts.builder()
            .setSubject(userDetails.username)
            .setExpiration(Date.from(accessExpirationInstant))
            .signWith(getSignInKey(jwtAccessSecret))
            .claim("roles", userDetails.authorities)
            .compact()
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val now: LocalDateTime = LocalDateTime.now()
        val accessExpirationInstant: Instant = now.plusDays(1L)
            .atZone(ZoneId.systemDefault()).toInstant()
        return Jwts.builder()
            .setSubject(userDetails.username)
            .setExpiration(Date.from(accessExpirationInstant))
            .signWith(getSignInKey(jwtRefreshSecret))
            .compact()
    }

    fun validateAccessToken(token: String): Boolean {
        return validateToken(token, jwtAccessSecret)
    }

    fun validateRefreshToken(token: String): Boolean {
        return validateToken(token, jwtRefreshSecret)
    }

    private fun validateToken(token: String, jwtSecretToken: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(getSignInKey(jwtSecretToken))
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractAllClaims(token: String, jwtSecretToken: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey(jwtSecretToken))
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(jwtSecretToken: String): Key {
        return SecretKeySpec(Decoders.BASE64.decode(jwtSecretToken), SignatureAlgorithm.HS256.jcaName)
    }
}