package zunza.zunlog.request

import org.hibernate.validator.constraints.Length

data class CreatePostRequest(
    @field: Length(min = 2, max = 20, message = "제목은 2자 이상 20자 이하여야 합니다.")
    val title: String,
    val content: String,
    @field: Length(max = 150, message = "요약은 150자 이하여야 합니다.")
    val summary: String,
)

data class CreateCommentRequest(
    @field: Length(min = 2, max = 50, message = "댓글은 2자 이상 50자 이하여야 합니다.")
    val content: String
)

data class UpdateCommentRequest(
    @field: Length(min = 2, max = 50, message = "댓글은 2자 이상 50자 이하여야 합니다.")
    val content: String
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String
)