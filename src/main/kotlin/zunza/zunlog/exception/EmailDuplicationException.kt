package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class EmailDuplicationException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "이미 사용 중인 이메일입니다."
    }

    init {
        initErrorField("Email")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.CONFLICT.value()
    }
}