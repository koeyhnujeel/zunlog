package zunza.zunlog.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.model.Post
import zunza.zunlog.repository.PostRepository

@Service
class PostService(
    private val postRepository: PostRepository
) {

    fun writePost(createPostDTO: CreatePostDTO) {
        val post = Post.from(createPostDTO)
        postRepository.save(post)
    }

    fun getAllPost(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        return postRepository.findPostByCondition(condition, value, pageable)
    }

    fun getPost(id: Long): PostDTO {
        val post = postRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("존재하지 않는 게시글입니다.")
        }
        return PostDTO.from(post)
    }
}
