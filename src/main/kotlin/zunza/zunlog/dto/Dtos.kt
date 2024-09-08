package zunza.zunlog.dto

import zunza.zunlog.Enum.IsRead
import zunza.zunlog.model.Post
import zunza.zunlog.request.CreatePostRequest
import java.time.Instant

data class CreatePostDTO(
    val userId: Long,
    val title: String,
    val content: String,
) {
    companion object {
        fun of(userId: Long, createPostRequest: CreatePostRequest): CreatePostDTO {
            return CreatePostDTO(
                userId = userId,
                title = createPostRequest.title,
                content = createPostRequest.content
            )
        }
    }
}

data class PostDTO(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val viewCount: Long,
    val createdDt: Instant,
    val updatedDt: Instant,
) {
    companion object {
        fun from(post: Post): PostDTO {
            return PostDTO(
                id = post.id,
                title = post.title,
                content = post.content,
                writer = post.user.nickname,
                viewCount = post.viewCount,
                createdDt = post.createdDt,
                updatedDt = post.updatedDt,
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
                content = content)
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
    val viewCount: Long,
    val createdDt: Instant,
    val updatedDt: Instant,
    val comments: List<CommentDTO>
) {
    companion object {
        fun of(postDTO: PostDTO, commentsDTO: List<CommentDTO>): PostDetailDTO {
            return PostDetailDTO(
                id = postDTO.id,
                title = postDTO.title,
                content = postDTO.content,
                writer = postDTO.writer,
                viewCount = postDTO.viewCount,
                createdDt = postDTO.createdDt,
                updatedDt = postDTO.updatedDt,
                comments = commentsDTO
            )
        }
    }
}
