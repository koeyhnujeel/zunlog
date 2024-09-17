package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.jwt.JwtUtil
import zunza.zunlog.request.TokenRequest

@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshAccessToken(@RequestBody tokenRequest: TokenRequest): Map<String, String> {
        val username = jwtUtil.getUsername(tokenRequest.refreshToken, jwtUtil.refreshSecretKey)
        jwtUtil.validateRefreshToken(tokenRequest.refreshToken, username)

        val expiredAccessTokenClaims = jwtUtil.getExpiredTokenClaims(tokenRequest.accessToken, jwtUtil.secretKey)
        val newAccessTokenClaims = hashMapOf(
            "userId" to expiredAccessTokenClaims.get("userId", String::class.java),
            "email" to expiredAccessTokenClaims.get("email", String::class.java),
            "role" to expiredAccessTokenClaims.get("role", String::class.java)
        )

        val accessToken = jwtUtil.generateAccessToken(username, newAccessTokenClaims)
        return mapOf("token" to accessToken)
    }
}