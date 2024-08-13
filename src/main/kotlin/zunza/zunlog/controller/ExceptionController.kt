package zunza.zunlog.controller

import org.springframework.http.ResponseEntity
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
}
