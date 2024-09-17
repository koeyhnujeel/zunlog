package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class NicknameDuplicationException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "이미 사용 중인 닉네임입니다."
    }

    init {
        initErrorField("Nickname")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.CONFLICT.value()
    }
}