package zunza.zunlog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zunza.zunlog.dto.LikeDTO
import zunza.zunlog.exception.PostNotFoundException
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.PostLike
import zunza.zunlog.repository.LikeRepository
import zunza.zunlog.repository.PostRepository
import zunza.zunlog.repository.UserRepository

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {

    fun likePost(likeDTO: LikeDTO) {
        val user = userRepository.findById(likeDTO.userId).orElseThrow { UserNotFoundException() }
        val post = postRepository.findById(likeDTO.postId).orElseThrow { PostNotFoundException() }

        val like = PostLike.of(user, post)
        likeRepository.save(like)
    }

    @Transactional
    fun unLikePost(likeDTO: LikeDTO) {
        likeRepository.deleteByUserIdAndPostId(likeDTO.userId, likeDTO.postId)
    }
}
