package zunza.zunlog.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import zunza.zunlog.exception.CustomException

@RestControllerAdvice
class ExceptionController {

    data class ErrorResponse(
        val message: String,
        val errorField: String,
        val code: Int
    )

    @ExceptionHandler(CustomException::class)
    fun customExceptionHandler(e: CustomException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            e.message!!,
            e.getErrorField(),
            e.getStatusCode()
        )
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse)
    }

    //TODO 수정하기
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<List<ErrorResponse>> {
        val errorResponses = e.fieldErrors.stream().map { error ->
            ErrorResponse(error.defaultMessage ?: "알 수 없는 오류입니다.", error.field, e.statusCode.value())
        }.toList()

        return ResponseEntity.status(e.statusCode.value()).body(errorResponses)
    }
}