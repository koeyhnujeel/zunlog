package zunza.zunlog.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import zunza.zunlog.service.UserDetailsService
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey
import kotlin.collections.HashMap

@Component
class JwtUtil(
    private val userDetailsService: UserDetailsService
) {
    val secretKey = "abcdefghijklmnopqrstuvwxyz1234567890"
    val refreshSecretKey = "refreshkeyabcdefghijklmnopqrstuvwxyz1234567890"

    fun generateAccessToken(username: String, claims: HashMap<String, String>): String {
        val key = getSecretKey(secretKey)
        val now = Instant.now()

        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(Date(now.toEpochMilli()))
            .expiration(Date(now.plusSeconds(600).toEpochMilli()))
            .signWith(key, Jwts.SIG.HS256).compact()
    }

    fun generateRefreshToken(username: String): String {
        val key = getSecretKey(refreshSecretKey)
        val now = Instant.now()

        return Jwts.builder()
            .subject(username)
            .issuedAt(Date(now.toEpochMilli()))
            .expiration(Date(now.plusSeconds(24 * 3600).toEpochMilli()))
            .signWith(key, Jwts.SIG.HS256).compact()
    }

    fun getClaims(token: String, secret: String): Jws<Claims> {
        val key = getSecretKey(secret)
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
    }

    fun getExpiredTokenClaims(token: String, secret: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    private fun getSecretKey(key: String): SecretKey {
        return Keys.hmacShaKeyFor(key.toByteArray())
    }

    fun getUsername(token: String, secret: String): String {
        return getClaims(token, secret).payload.subject
    }

    fun isTokenExpired(token: String, secret: String): Boolean {
        try {
            return Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .payload
                .expiration
                .before(Date())
        } catch (e: ExpiredJwtException) {
            throw ExpiredJwtException(null, null, "토큰이 만료되었습니다.")
        } catch (e: SignatureException) {
            throw SignatureException("토큰이 유효하지 않습니다.")
        }
    }

    fun validateAccessToken(token: String, username: String): Boolean {
        return try {
            getClaims(token, secretKey).payload.subject == username
        } catch (e: Exception) {
            false
        }
    }

    fun validateRefreshToken(token: String, username: String): Boolean {
        return try {
            !isTokenExpired(token, refreshSecretKey) && getClaims(token, refreshSecretKey).payload.subject == username
        } catch (e: Exception) {
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token, secretKey)
        val userId = claims.payload.get("userId", String::class.java).toLong()
        val role = claims.payload.get("role", String::class.java)
        return UsernamePasswordAuthenticationToken(userId, null, mutableListOf(GrantedAuthority { role }))
    }
}