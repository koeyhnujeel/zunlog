package zunza.zunlog.dto

import zunza.zunlog.model.Post
import zunza.zunlog.model.User
import zunza.zunlog.request.CreatePostRequest
import java.time.Instant

data class CreatePostDTO(
    val user: User,
    val title: String,
    val content: String,
) {
    companion object {
        fun of(user: User, createPostRequest: CreatePostRequest): CreatePostDTO {
            return CreatePostDTO(
                user = user,
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
