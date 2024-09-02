package zunza.zunlog.service

import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreateCommentDTO
import zunza.zunlog.exception.PostNotFoundException
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.Comment
import zunza.zunlog.repository.CommentRepository
import zunza.zunlog.repository.PostRepository
import zunza.zunlog.repository.UserRepository

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {
    fun writeComment(createCommentDTO: CreateCommentDTO) {
        val user = userRepository.findById(createCommentDTO.userId).orElseThrow {
            throw UserNotFoundException()
        }

        val post = postRepository.findById(createCommentDTO.postId).orElseThrow {
            throw PostNotFoundException()
        }

        val comment = Comment.of(createCommentDTO.content, user, post)
        commentRepository.save(comment)
    }


}
