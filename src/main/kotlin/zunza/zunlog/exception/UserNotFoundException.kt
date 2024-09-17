package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class UserNotFoundException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "존재하지 않는 유저입니다."
    }

    init {
        initErrorField("User ID")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.NOT_FOUND.value()
    }
}