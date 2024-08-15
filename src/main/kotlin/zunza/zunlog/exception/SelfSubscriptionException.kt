package zunza.zunlog.exception

class SelfSubscriptionException(): CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "자신을 구독할 순 없습니다."
    }

    init {
        initErrorField("Target ID")
    }

    override fun getStatusCode(): Int {
        return 400
    }
}
