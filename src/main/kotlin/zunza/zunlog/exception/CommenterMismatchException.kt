package zunza.zunlog.exception

class CommenterMismatchException(): CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "댓글 작성자가 아닙니다."
    }

    init {
        initErrorField("User ID")
    }

    override fun getStatusCode(): Int {
        return 403
    }
}
