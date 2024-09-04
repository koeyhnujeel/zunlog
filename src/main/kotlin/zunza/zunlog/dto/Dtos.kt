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
    val createdDt: Instant,
    val updatedDt: Instant
) {
    companion object {
        fun from(post: Post): PostDTO {
            return PostDTO(
                id = post.id,
                title = post.title,
                content = post.content,
                writer = post.user.nickname,
                createdDt = post.createdDt,
                updatedDt = post.updatedDt
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
) {
//    companion object {
//        fun from(notification: Notification): NotificationDTO {
//            return NotificationDTO(
//                id = notification.id,
//                message = notification.message,
//                isRead = notification.isRead.name)
//        }
//    }
}

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
)
