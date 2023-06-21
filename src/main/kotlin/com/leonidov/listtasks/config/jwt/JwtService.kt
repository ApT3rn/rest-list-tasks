package com.leonidov.listtasks.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val SECRET_KEY: String = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDLPiAPYilpbrxaP7iGtjNvZ2G85lCBJGQ+J+coF5fe6lDWmTKPLc6AaFVwQSjCCHNrfnYkds8HasJihSoosCUyB3NRuoqC9FCxnhi+QzWN1cY5X1v9VOAyibyrSWLO8tN6vqL/rkfmMbUQzZOYM296aFZq9RV9Sxa8uoc4w8UYBwaj5ZlDGKeTU9/vxvtJ4Q+2Bbb/ZK8foyVmduP2eN6pjPEWvfmDWi+EJw7flD14/r9g26dw6T+Uo1RHT9YVX1mPprhuMtpgypsXZb46bF6XLMqJ+B95NlYyh2IVfoFTY0LS+cEE5pkFpt0gxUvKslRgNuu2aCMNMdAspXq2bDUzAgMBAAECggEBAKuw8J5uOzOQ01aeC8DpPCw5NkzAd3ORDYmgZdrti2NJqD6RLi/b7110RfDNaqP3RaGiNCV7pErMuidF/SZgHxvTRWzeW9Jil6RkPNzFpAEMfSbMUsaAI+EtkA9WsDzIzzQaxVGepahlh2MJd0yU7vCGIbK7FYtBmpSWMlMWtnIjQ9/t8y3olPijNEPuuSrf8jpBJiezQHcKBL21tDhz/Yazjr90wNXKZ4nFFz9kBy1oW8dw8u/4CrWibN7waqaTJ/p5+PSjsK/OL6pTH20v/PPkMQPCsariB9FxMl48Rn2nC2vvY30y0DF0XtMxYsK3A52KPzE/M/mUQdbdi8Ao5BECgYEA534o+CzS0vuXu7ppg2tDj2A1OqrWBxN6nDraPBN7pd17q4Vn+2NV7Smgkt9XoEWECY2jsyOlP2xNvdQZ93dGrn229U2HHORM+Bx3L9KQJ6tGcC5KVyCmpMTnzMMgednNFk5Vyb5FCtCnrZDtHzNvnKzaDw9jeDRC8M13XmZlSwsCgYEA4MJW7YqT0LGSu9WUFQhqeYXpaqFWW69aFVI9LxCTkntH3GYW9hJ2ZtrZMbtxoDuBEULyEKSSbPQCGmnYbwyQtJaH4HgHlMKcL3cL3VAzp3wXFgUU10dkyJLrs0UCmE8d+NODbecQnkF6pWEmaIbrm8+BVTZe2BgxflvxB2sBV3kCgYBadRD7UUCckxWKlOAwK1T+DKEgKKtznmf17oVinlZdzPtLXdjxAbMM9ow5eC6cgS2EMiW6lX44F1EVkXEII0KqYpOArWfToV52QWFNfaE47xOaOGxyOV8E2bCecc0Z8ELUqY2aRNBj1n7/MreFzqqZdW3MHr5GtV1HDXLMn4SLWwKBgQDU50JwwVRD2/Gi/C3Jvq8dbjwtRacIMpKin8RVUN55Y04u+IXHkrWVuLkE4wo/Ph6Eu2WRXlNV4c4cNydOChkI946+kvSbdb19OMeEXPSRMnqr4SRcyMQvOv0KU9ShclBx4+obsUWMo3oKLnyzhxqn/WgFz/sHt+MqTu2CmVosAQKBgQCZHtxlUlavt5bZtcUvuAcnIsIJU3i5zu+91axsxnc198WuiJfgrBY4BcwpGTjaaQXqO9nj5/JjsseDyT1HfTk1EbRpokkBwOmlONnjKXQ1y+x8llLPNKQi1iJ6w/FLkd+9rdX5N6PLE/oMZtCqgDJIUNtC+iO4bE1OGdjkeNHxaw=="

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)


    fun <T> extractClaim(token: String, claimsResolver: java.util.function.Function<Claims, T>) : T {
        val claims: Claims = extractAllClaims(token);
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails): String = generateToken(HashMap(), userDetails)


    fun generateToken(extraClaims: Map<String, Objects>, userDetails: UserDetails): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() * 1000 * 60 * 1))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun inTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username: String = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())


    private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)


    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes: ByteArray? = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}