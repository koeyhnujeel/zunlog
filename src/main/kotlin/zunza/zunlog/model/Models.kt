package zunza.zunlog.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.UpdatePostDTO
import java.time.Instant

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var content: String,
    val writer: String,
    var createdDt: Instant = Instant.now(),
    var updatedDt: Instant = Instant.now(),
) {

    fun update(updatePostDTO: UpdatePostDTO) {
        this.title = updatePostDTO.title
        this.content = updatePostDTO.content
    }

    companion object {
        fun from(createPostDTO: CreatePostDTO): Post {
            return Post(
                title = createPostDTO.title,
                content = createPostDTO.content,
                writer = createPostDTO.writer
            )
        }
    }
}
