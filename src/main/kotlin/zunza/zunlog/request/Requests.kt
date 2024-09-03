package zunza.zunlog.request

import org.hibernate.validator.constraints.Length


data class CreatePostRequest(
    val title: String,
    val content: String,
)

data class CreateCommentRequest(
    @field: Length(min = 2, max = 50, message = "댓글은 2자 이상 50자 이하여야 합니다.")
    val content: String
)

data class UpdateCommentRequest(
    @field: Length(min = 2, max = 50, message = "댓글은 2자 이상 50자 이하여야 합니다.")
    val content: String
)
