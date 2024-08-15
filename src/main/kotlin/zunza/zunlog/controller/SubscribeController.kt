package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreateSubscriptionDTO
import zunza.zunlog.config.UserDetails
import zunza.zunlog.service.SubscribeService

@RestController
@RequestMapping("/subscriptions")
class SubscribeController(
    private val subscribeService: SubscribeService
) {

    @PostMapping("/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable targetId: Long
    ) {
        val createSubscriptionDTO = CreateSubscriptionDTO.of(targetId, userDetails.getUserId())
        subscribeService.subscribe(createSubscriptionDTO)
    }
}
