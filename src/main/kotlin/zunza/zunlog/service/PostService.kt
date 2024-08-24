package zunza.zunlog.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.dto.UpdatePostDTO
import zunza.zunlog.event.PostEvent
import zunza.zunlog.exception.PostNotFoundException
import zunza.zunlog.model.Post
import zunza.zunlog.repository.PostRepository

@Service
class PostService(
    private val postRepository: PostRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun writePost(createPostDTO: CreatePostDTO) {
        val startTime = System.nanoTime()
        println("시작 시간: ${startTime}ms")

        val post = Post.from(createPostDTO)
        postRepository.save(post)
        eventPublisher.publishEvent(PostEvent(createPostDTO.user.id))

        val endTime = System.nanoTime()
        println("끝 시간: ${endTime}ms")
        val durationInMillis = (endTime - startTime) / 1_000_000
        println("측정 시간: ${durationInMillis}ms")
    }

    fun getAllPost(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        return postRepository.findPostByCondition(condition, value, pageable)
    }

    fun getPost(id: Long): PostDTO {
        val post = postRepository.findByIdFetchUser(id) ?: throw PostNotFoundException()
        return PostDTO.from(post)
    }

    @Transactional
    fun updatePost(id: Long, updatePostDTO: UpdatePostDTO) {
        val post = postRepository.findById(id).orElseThrow {
            throw PostNotFoundException()
        }
        post.update(updatePostDTO)
    }

    fun deletePost(id: Long) {
        val post = postRepository.findById(id).orElseThrow {
            throw PostNotFoundException()
        }
        postRepository.deleteById(post.id)
    }
}
