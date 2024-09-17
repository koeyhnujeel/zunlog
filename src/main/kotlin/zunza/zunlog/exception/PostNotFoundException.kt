package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class PostNotFoundException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "존재하지 않는 게시글입니다."
    }

    init {
        initErrorField("Post ID")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.NOT_FOUND.value()
    }
}