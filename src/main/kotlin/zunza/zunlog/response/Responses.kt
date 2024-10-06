package zunza.zunlog.response

import zunza.zunlog.Enum.IsLike

data class LikeResponse(
    val likeCount: Int,
    val isLike: IsLike
)