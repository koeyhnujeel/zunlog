package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class SelfSubscriptionException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "자신을 구독할 순 없습니다."
    }

    init {
        initErrorField("Target ID")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.BAD_REQUEST.value()
    }
}