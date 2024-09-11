package zunza.zunlog.exception

class EmailDuplicationException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "이미 사용 중인 이메일입니다."
    }

    init {
        initErrorField("Email")
    }

    override fun getStatusCode(): Int {
        return 409
    }
}