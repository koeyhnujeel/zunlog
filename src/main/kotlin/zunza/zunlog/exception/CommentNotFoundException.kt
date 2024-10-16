package zunza.zunlog.exception

import org.springframework.http.HttpStatus

class CommentNotFoundException() : CustomException(MESSAGE) {

    companion object {
        private const val MESSAGE = "존재하지 않는 댓글입니다."
    }

    init {
        initErrorField("Comment ID")
    }

    override fun getStatusCode(): Int {
        return HttpStatus.NOT_FOUND.value()
    }
}