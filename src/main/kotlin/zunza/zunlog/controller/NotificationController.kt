package zunza.zunlog.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.config.UserDetails
import zunza.zunlog.service.NotificationService

@RestController
class NotificationController(
    private val notificationService: NotificationService
) {
    @GetMapping(value = ["/connect"], produces = ["text/event-stream"])
    fun subscribe(@AuthenticationPrincipal userDetails: UserDetails): SseEmitter {
        return notificationService.subscribe(userDetails.getUserId())
    }
}
