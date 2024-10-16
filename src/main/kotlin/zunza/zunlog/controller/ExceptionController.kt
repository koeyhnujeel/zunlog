package zunza.zunlog.controller

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import zunza.zunlog.dto.ErrorDTO
import zunza.zunlog.exception.CustomException

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(CustomException::class)
    fun customExceptionHandler(e: CustomException): ResponseEntity<ErrorDTO> {
        val errorDTO = ErrorDTO(
            e.message!!,
            e.getErrorField(),
            e.getStatusCode()
        )
        return ResponseEntity.status(e.getStatusCode()).body(errorDTO)
    }

    //TODO 수정하기
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<List<ErrorDTO>> {
        val errorResponses = e.fieldErrors.stream().map { error ->
            ErrorDTO(error.defaultMessage ?: "알 수 없는 오류입니다.", error.field, e.statusCode.value())
        }.toList()

        return ResponseEntity.status(e.statusCode.value()).body(errorResponses)
    }
}