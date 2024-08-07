package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreatePostDTO
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
}
