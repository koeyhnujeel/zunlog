package zunza.zunlog.dto

import zunza.zunlog.model.Post
import java.time.Instant

data class CreatePostDTO(
    val title: String,
    val content: String,
    val writer: String
)

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
                writer = post.writer,
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
