package zunza.zunlog.event

abstract class CustomEvent {
    abstract val senderId: Long
    abstract fun getEventName(): String
    abstract fun getMessage(): String
}

class PostEvent(override val senderId: Long) : CustomEvent() {
    override fun getMessage(): String {
        return "${senderId}님이 새 글을 올렸습니다!"
    }

    override fun getEventName(): String {
        return "new post"
    }
}
