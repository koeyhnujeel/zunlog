package zunza.zunlog.controller

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.jwt.JwtUtil
import zunza.zunlog.security.MemberDetailsService


data class AuthRequest(
    val email: String,
    val password: String,
)
@RestController
class AuthController(
    private val memberDetailsService: MemberDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody authRequest: AuthRequest): String {
        val userDetails = memberDetailsService.loadUserByUsername(authRequest.email)

        val claims = hashMapOf(
            "email" to authRequest.email,
            "role" to userDetails.authorities.first().authority
        )

        if (passwordEncoder.matches(authRequest.password, userDetails.password)) {
            return jwtUtil.generateToken(authRequest.email, claims)
        }

        return "Authentication Failed"
    }
}
