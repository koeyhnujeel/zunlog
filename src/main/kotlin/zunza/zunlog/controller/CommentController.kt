package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreateCommentDTO
import zunza.zunlog.request.CreateCommentRequest
import zunza.zunlog.service.CommentService

@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        @AuthenticationPrincipal userId: Long,
        @PathVariable postId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ) {
        val createCommentDTO = CreateCommentDTO.of(userId, postId, createCommentRequest.content)
        commentService.writeComment(createCommentDTO)
    }
}
