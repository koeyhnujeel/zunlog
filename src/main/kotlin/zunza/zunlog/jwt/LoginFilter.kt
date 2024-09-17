package zunza.zunlog.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import zunza.zunlog.config.UserDetails
import zunza.zunlog.dto.ErrorDTO
import zunza.zunlog.dto.TokenDTO
import zunza.zunlog.request.LoginRequest
import java.nio.charset.StandardCharsets

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val objectMapper: ObjectMapper,
    private val jwtUtil: JwtUtil
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val loginRequest = objectMapper.readValue(request?.inputStream, LoginRequest::class.java)
        val email = loginRequest.email
        val password = loginRequest.password
        val token = UsernamePasswordAuthenticationToken(email, password, null)
        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val userDetails = authResult?.principal as UserDetails

        val claims = hashMapOf(
            "userId" to userDetails.getUserId().toString(),
            "email" to userDetails.username,
            "role" to userDetails.authorities.first().authority
        )

        val accessToken = jwtUtil.generateAccessToken(userDetails.username, claims)
        val refreshToken = jwtUtil.generateRefreshToken(userDetails.username)
        val tokenDTO = TokenDTO.of(accessToken, refreshToken)

        with(response!!) {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = StandardCharsets.UTF_8.name()
            status = HttpStatus.OK.value()
            objectMapper.writeValue(writer, tokenDTO)
        }
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        val errorDTO = ErrorDTO(
            "아이디 또는 비밀번호를 확인해 주세요.",
            "Email or Password",
            HttpStatus.UNAUTHORIZED.value()
        )

        with(response!!) {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = StandardCharsets.UTF_8.name()
            status = HttpStatus.UNAUTHORIZED.value()
            objectMapper.writeValue(writer, errorDTO)
        }
    }
}