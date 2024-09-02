package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.config.UserDetails
import zunza.zunlog.jwt.JwtUtil
import zunza.zunlog.service.UserDetailsService


data class AuthRequest(
    val email: String,
    val password: String,
)
@RestController
class AuthController(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody authRequest: AuthRequest): HashMap<String, String> {
        val userDetails = (userDetailsService.loadUserByUsername(authRequest.email) as UserDetails)

        val claims = hashMapOf(
            "userId" to userDetails.getUserId().toString(),
            "email" to authRequest.email,
            "role" to userDetails.authorities.first().authority
        )

        val response = hashMapOf<String, String>()
        if (passwordEncoder.matches(authRequest.password, userDetails.password)) {
            response["token"] = jwtUtil.generateToken(authRequest.email, claims)
            return response
        }

        response["token"] = "Authentication Failed"
        return response
    }
}
