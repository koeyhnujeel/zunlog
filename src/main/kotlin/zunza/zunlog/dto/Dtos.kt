package zunza.zunlog.dto

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
)
