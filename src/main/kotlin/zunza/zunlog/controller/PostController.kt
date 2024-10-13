package zunza.zunlog.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
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
import zunza.zunlog.dto.*
import zunza.zunlog.request.CreatePostRequest
import zunza.zunlog.service.PostService

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(
        @AuthenticationPrincipal userId: Long,
        @Valid @RequestBody createPostRequest: CreatePostRequest
    ) {
        val createPostDTO = CreatePostDTO.of(userId, createPostRequest)
        postService.writePost(createPostDTO)
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    fun getPostsV1(
//        @RequestParam page: Int = 1,
//        @RequestParam size: Int = 25
//    ): Page<PostListDTO> {
//
//        val pageable = PageRequest.of(page - 1, size)
//        return postService.getPostListV1(pageable)
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getPostsV2(
        @RequestParam currentPage: Int = 1,
        @RequestParam targetPage: Int = 0,
        @RequestParam size: Long = 25L,
        @RequestParam lastPostId: Int = 0
    ): Page<PostListDTO> {

        val pageDTO = PageDTO(currentPage, targetPage, size, lastPostId)
        return postService.getPostListV2(pageDTO)
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchPosts(
        @RequestParam condition: String = "title",
        @RequestParam value: String = "",
        @RequestParam currentPage: Int = 1,
        @RequestParam targetPage: Int = 0,
        @RequestParam size: Long = 25L,
        @RequestParam lastPostId: Int = 0
    ): Page<PostListDTO> {

        val pageDTO = PageDTO(currentPage, targetPage, size, lastPostId)
        return postService.search(condition, value, pageDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPost(
        @AuthenticationPrincipal userId: Long = -1,
        @PathVariable id: Long
    ): PostDetailDTOv2 {
        return postService.getPost(userId, id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePost(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long,
        @RequestBody updatePostDTO: UpdatePostDTO
    ) {
        postService.updatePost(userId, id, updatePostDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePost(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long
    ) {
        postService.deletePost(userId, id)
    }
}