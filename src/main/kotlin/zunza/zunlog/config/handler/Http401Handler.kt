package zunza.zunlog.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import zunza.zunlog.dto.ErrorDTO
import java.nio.charset.StandardCharsets

class Http401Handler(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val errorDTO = ErrorDTO(
            "로그인이 필요한 서비스입니다.",
            "",
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