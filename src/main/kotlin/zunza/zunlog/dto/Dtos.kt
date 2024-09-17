package zunza.zunlog.dto

import zunza.zunlog.Enum.IsRead
import zunza.zunlog.model.Post
import zunza.zunlog.request.CreatePostRequest
import java.time.Instant

data class CreatePostDTO(
    val userId: Long,
    val title: String,
    val content: String,
    val summary: String
) {
    companion object {
        fun of(userId: Long, createPostRequest: CreatePostRequest): CreatePostDTO {
            return CreatePostDTO(
                userId = userId,
                title = createPostRequest.title,
                content = createPostRequest.content,
                summary = createPostRequest.summary
            )
        }
    }
}

data class PostDTO(
    val id: Long,
    val title: String,
    val summary: String,
    val writer: String,
    val likeCount: Int,
    val commentCount: Int,
    val createdDt: Instant,
) {
    companion object {
        fun from(post: Post): PostDTO {
            return PostDTO(
                id = post.id,
                title = post.title,
                summary = post.content,
                writer = post.user.nickname,
                likeCount = post.likes.size,
                commentCount = post.comments.size,
                createdDt = post.createdDt,
            )
        }
    }
}

data class UpdatePostDTO(
    val title: String,
    val content: String
)

data class CreateUserDTO(
    val email: String,
    val password: String,
    val nickname: String,
)

data class EmailDuplicationDTO(
    val message: String = "사용 가능한 이메일입니다."
)

data class NicknameDuplicationDTO(
    val message: String = "사용 가능한 닉네임입니다."
)

data class CreateSubscriptionDTO(
    val targetId: Long,
    val subscriberId: Long
) {
    companion object {
        fun of(targetId: Long, subscriberId: Long): CreateSubscriptionDTO {
            return CreateSubscriptionDTO(targetId = targetId, subscriberId = subscriberId)
        }
    }
}

data class RemoveSubscriptionDTO(
    val targetId: Long,
    val subscriberId: Long
) {
    companion object {
        fun of(targetId: Long, subscriberId: Long): RemoveSubscriptionDTO {
            return RemoveSubscriptionDTO(targetId = targetId, subscriberId = subscriberId)
        }
    }
}

data class NotificationDTO(
    val id: Long,
    val message: String,
    val isRead: IsRead,
)

data class UnreadNotificationCountDTO(
    val count: Int,
) {
    companion object {
        fun from(count: Int): UnreadNotificationCountDTO {
            return UnreadNotificationCountDTO(count)
        }
    }
}

data class CreateCommentDTO(
    val userId: Long,
    val postId: Long,
    val content: String
) {
    companion object {
        fun of(userId: Long, postId: Long, content: String): CreateCommentDTO {
            return CreateCommentDTO(
                userId = userId,
                postId = postId,
                content = content
            )
        }
    }
}

data class UpdateCommentDTO(
    val userId: Long,
    val commentId: Long,
    val content: String
) {
    companion object {
        fun of(userId: Long, commentId: Long, content: String): UpdateCommentDTO {
            return UpdateCommentDTO(
                userId = userId,
                commentId = commentId,
                content = content
            )
        }
    }
}

data class DeleteCommentDTO(
    val userId: Long,
    val commentId: Long
) {
    companion object {
        fun of(userId: Long, commentId: Long): DeleteCommentDTO {
            return DeleteCommentDTO(
                userId = userId,
                commentId = commentId
            )
        }
    }
}

data class CommentDTO(
    val commentId: Long,
    val content: String,
    val writer: String,
    val createdDt: Instant,
)

data class PostDetailDTO(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val createdDt: Instant,
    val updatedDt: Instant,
    val comments: List<CommentDTO>,
    val likeCount: Int
) {
//    companion object {
//        fun of(postDTO: PostDTO, commentsDTO: List<CommentDTO>): PostDetailDTO {
//            return PostDetailDTO(
//                id = postDTO.id,
//                title = postDTO.title,
//                content = postDTO.summary,
//                writer = postDTO.writer,
//                createdDt = postDTO.createdDt,
//                updatedDt = postDTO.updatedDt,
//                comments = commentsDTO,
//                likeCount = postDTO.likeCount
//            )
//        }
//    }
}

data class LikeDTO(
    val userId: Long,
    val postId: Long
) {
    companion object {
        fun of(userId: Long, postId: Long): LikeDTO {
            return LikeDTO(
                userId = userId,
                postId = postId
            )
        }
    }
}

data class TokenDTO(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(accessToken: String, refreshToken: String): TokenDTO {
            return TokenDTO(accessToken = accessToken, refreshToken = refreshToken)
        }
    }
}

data class ErrorDTO(
    val message: String,
    val errorField: String,
    val code: Int
)