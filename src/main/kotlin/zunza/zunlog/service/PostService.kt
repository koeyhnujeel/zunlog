package zunza.zunlog.service

import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreatePostDTO
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
}
