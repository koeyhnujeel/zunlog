package zunza.zunlog.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.dto.PostDetailDTO
import zunza.zunlog.dto.UpdatePostDTO
import zunza.zunlog.event.PostEvent
import zunza.zunlog.exception.AuthorMismatchException
import zunza.zunlog.exception.PostNotFoundException
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.Post
import zunza.zunlog.repository.PostRepository
import zunza.zunlog.repository.UserRepository

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun writePost(createPostDTO: CreatePostDTO) {
        val user = userRepository.findById(createPostDTO.userId).orElseThrow { throw UserNotFoundException() }
        val post = Post.of(user, createPostDTO.title, createPostDTO.content, createPostDTO.summary)

        postRepository.save(post)
        eventPublisher.publishEvent(PostEvent(user.id))
    }

    fun getAllPost(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        return postRepository.findPostByCondition(condition, value, pageable)
    }

    fun getPost(id: Long): PostDetailDTO {
        return postRepository.findByIdWithUserAndCommentV2(id) ?: throw PostNotFoundException()
    }

    @Transactional
    fun updatePost(userId: Long, id: Long, updatePostDTO: UpdatePostDTO) {
        val post = postRepository.findById(id).orElseThrow { throw PostNotFoundException() }

        isWriter(userId, post.user.id)
        post.update(updatePostDTO)
    }

    fun deletePost(userId: Long, id: Long) {
        val post = postRepository.findById(id).orElseThrow { throw PostNotFoundException() }

        isWriter(userId, post.user.id)
        postRepository.deleteById(post.id)
    }

    private fun isWriter(userId: Long, authorId: Long) {
        if (userId != authorId) {
            throw AuthorMismatchException()
        }
    }
}