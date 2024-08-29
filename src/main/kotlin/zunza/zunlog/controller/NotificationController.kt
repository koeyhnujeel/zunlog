package zunza.zunlog.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.config.UserDetails
import zunza.zunlog.dto.NotificationDTO
import zunza.zunlog.dto.UnreadNotificationCountDTO
import zunza.zunlog.service.NotificationService

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {
    @GetMapping(value = ["/connect"], produces = ["text/event-stream"])
    fun subscribe(@AuthenticationPrincipal userDetails: UserDetails): SseEmitter {
        return notificationService.subscribe(userDetails.getUserId())
    }

    @GetMapping("/unread/count")
    @ResponseStatus(HttpStatus.OK)
    fun getUnreadNotificationCount(@AuthenticationPrincipal userDetails: UserDetails): UnreadNotificationCountDTO {
        return notificationService.getUnreadNotificationCount(userDetails.getUserId())
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getNotifications(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam page: Int = 1,
    ): Page<NotificationDTO> {

        val pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdDt"))
        return notificationService.getAllNotifications(userDetails.getUserId(), pageable)
    }

    @PostMapping("/{notificationId}/read")
    @ResponseStatus(HttpStatus.OK)
    fun readNotification(
        @PathVariable notificationId: Long
    ) {
        notificationService.read(notificationId)
    }
}
