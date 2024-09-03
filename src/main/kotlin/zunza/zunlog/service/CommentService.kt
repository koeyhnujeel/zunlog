package zunza.zunlog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zunza.zunlog.dto.CreateCommentDTO
import zunza.zunlog.dto.UpdateCommentDTO
import zunza.zunlog.exception.CommentNotFoundException
import zunza.zunlog.exception.CommenterMismatchException
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

    @Transactional
    fun updateComment(updateCommentDTO: UpdateCommentDTO) {
        val user = userRepository.findById(updateCommentDTO.userId).orElseThrow {
            throw UserNotFoundException()
        }

        val comment = commentRepository.findById(updateCommentDTO.commentId).orElseThrow {
            throw CommentNotFoundException()
        }

        isCommenter(user.id, comment.user.id)
        comment.updateComment(updateCommentDTO.content)
    }

    private fun isCommenter(userId: Long, commenterId: Long) {
        if (userId != commenterId) {
            throw CommenterMismatchException()
        }
    }
}
