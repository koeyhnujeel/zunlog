package zunza.zunlog.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import zunza.zunlog.dto.ErrorDTO
import java.nio.charset.StandardCharsets

@Component
class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            setResponse(response, e.message!!)
        } catch (e: SignatureException) {
            setResponse(response, e.message!!)
        }
    }

    private fun setResponse(response: HttpServletResponse, message: String) {
        val errorDTO = ErrorDTO(
            message,
            "Token",
            401
        )

        with(response) {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = StandardCharsets.UTF_8.name()
            status = errorDTO.code
            objectMapper.writeValue(writer, errorDTO)
        }
    }
}