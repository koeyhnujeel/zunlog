package zunza.zunlog.exception

class NotificationNotFoundException(): CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "존재하지 않는 알림입니다."
    }

    init {
        initErrorField("Notification ID")
    }

    override fun getStatusCode(): Int {
        return 404
    }
}
