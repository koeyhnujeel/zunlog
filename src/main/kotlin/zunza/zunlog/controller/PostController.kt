package zunza.zunlog.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.dto.UpdatePostDTO
import zunza.zunlog.request.CreatePostRequest
import zunza.zunlog.config.UserDetails
import zunza.zunlog.service.PostService

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody createPostRequest: CreatePostRequest
    ) {
        val createPostDTO = CreatePostDTO.of(userDetails.getUser(), createPostRequest)
        postService.writePost(createPostDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getPosts(
        @RequestParam condition: String = "title",
        @RequestParam value: String = "",
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): List<PostDTO> {

        val pageable = PageRequest.of(page - 1, size)
        return postService.getAllPost(condition, value, pageable)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPost(@PathVariable id: Long): PostDTO {
        return postService.getPost(id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePost(@PathVariable id: Long, @RequestBody updatePostDTO: UpdatePostDTO) {
        postService.updatePost(id, updatePostDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePost(@PathVariable id: Long) {
        postService.deletePost(id)
    }
}
