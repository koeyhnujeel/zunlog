package zunza.zunlog.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.UpdatePostDTO
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var content: String,
    val writer: String,
    val createdDt: Instant = Instant.now(),
    @LastModifiedDate
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
