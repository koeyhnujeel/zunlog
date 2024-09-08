package zunza.zunlog.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreateCommentDTO
import zunza.zunlog.dto.DeleteCommentDTO
import zunza.zunlog.dto.UpdateCommentDTO
import zunza.zunlog.request.CreateCommentRequest
import zunza.zunlog.request.UpdateCommentRequest
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
        @Valid @RequestBody createCommentRequest: CreateCommentRequest
    ) {
        val createCommentDTO = CreateCommentDTO.of(userId, postId, createCommentRequest.content)
        commentService.writeComment(createCommentDTO)
    }

    @PutMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateComment(
        @AuthenticationPrincipal userId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody updateCommentRequest: UpdateCommentRequest
    ) {
        val updateCommentDTO = UpdateCommentDTO.of(userId, commentId, updateCommentRequest.content)
        commentService.updateComment(updateCommentDTO)
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        @AuthenticationPrincipal userId: Long,
        @PathVariable commentId: Long
    ) {
        val deleteCommentDTO = DeleteCommentDTO.of(userId, commentId)
        commentService.deleteComment(deleteCommentDTO)
    }
}