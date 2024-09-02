package zunza.zunlog.request


data class CreatePostRequest(
    val title: String,
    val content: String,
)

data class CreateCommentRequest(
    val content: String
)
