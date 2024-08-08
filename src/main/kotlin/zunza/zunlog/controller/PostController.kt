package zunza.zunlog.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreatePostDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.service.PostService

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(
        @RequestBody createPostDTO: CreatePostDTO
    ) {
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
}
