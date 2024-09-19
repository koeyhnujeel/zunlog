package zunza.zunlog.event

abstract class CustomEvent {
    abstract val senderId: Long
    abstract val senderNickname: String
    abstract fun getEventName(): String
    abstract fun getMessage(): String
    abstract fun getType(): String
    open fun getReceiverId(): Long = -1
    open fun getReferenceId(): Long = -1
}

class PostEvent(
    override val senderId: Long,
    override val senderNickname: String,
    private val referenceId: Long
) : CustomEvent() {
    override fun getMessage(): String = "${senderNickname}님이 새 글을 올렸습니다!"
    override fun getEventName(): String = "new post"
    override fun getType(): String = "post"
    override fun getReferenceId(): Long = referenceId
}

class CommentEvent(
    override val senderId: Long,
    override val senderNickname: String,
    private val receiverId: Long,
    private val referenceId: Long
) : CustomEvent() {
    override fun getEventName(): String = "new comment"
    override fun getMessage(): String = "${senderNickname}님이 댓글을 다셨습니다!"
    override fun getReceiverId(): Long = receiverId
    override fun getType(): String = "comment"
    override fun getReferenceId(): Long = referenceId
}

class LikeEvent(
    override val senderId: Long,
    override val senderNickname: String,
    private val receiverId: Long,
    private val referenceId: Long
) : CustomEvent() {
    override fun getEventName(): String = "new like"
    override fun getMessage(): String = "${senderNickname}님이 좋아요를 눌러주셨습니다!"
    override fun getReceiverId(): Long = receiverId
    override fun getType(): String = "like"
    override fun getReferenceId(): Long = referenceId
}