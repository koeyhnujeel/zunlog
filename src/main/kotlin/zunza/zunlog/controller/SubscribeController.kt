package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreateSubscriptionDTO
import zunza.zunlog.config.UserDetails
import zunza.zunlog.dto.RemoveSubscriptionDTO
import zunza.zunlog.service.SubscriptionService

@RestController
@RequestMapping("/subscriptions")
class SubscribeController(
    private val subscriptionService: SubscriptionService
) {

    @PostMapping("/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable targetId: Long
    ) {
        val createSubscriptionDTO = CreateSubscriptionDTO.of(targetId, userDetails.getUserId())
        subscriptionService.subscribe(createSubscriptionDTO)
    }

    @DeleteMapping("/{targetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable targetId: Long
    ) {
        val removeSubscriptionDTO = RemoveSubscriptionDTO.of(targetId, userDetails.getUserId())
        subscriptionService.unsubscribe(removeSubscriptionDTO)
    }
}
