package zunza.zunlog.exception

class PostNotFoundException(): CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "존재하지 않는 게시글입니다."
    }

    init {
        initErrorField("Post ID")
    }

    override fun getStatusCode(): Int {
        return 404
    }
}
