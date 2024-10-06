package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.LikeDTO
import zunza.zunlog.response.LikeResponse
import zunza.zunlog.service.LikeService

@RestController
class LikeController(
    private val likeService: LikeService,
) {

    @PostMapping("/posts/{postId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    fun like(
        @AuthenticationPrincipal userId: Long,
        @PathVariable postId: Long
    ): LikeResponse {
        val likeDTO = LikeDTO.of(userId, postId)
        return likeService.likePost(likeDTO)
    }

    @DeleteMapping("/posts/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unLike(
        @AuthenticationPrincipal userId: Long,
        @PathVariable postId: Long
    ) {
        val likeDTO = LikeDTO.of(userId, postId)
        likeService.unLikePost(likeDTO)
    }
}