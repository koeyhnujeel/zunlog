package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class AuthorMismatchException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "작성자가 아닙니다."
    }

    init {
        initErrorField("User ID")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.FORBIDDEN.value()
    }
}