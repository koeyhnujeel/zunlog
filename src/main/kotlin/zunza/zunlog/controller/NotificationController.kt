package zunza.zunlog.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.dto.NotificationDTO
import zunza.zunlog.dto.UnreadNotificationCountDTO
import zunza.zunlog.service.NotificationService

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {
    @GetMapping(value = ["/connect"], produces = ["text/event-stream"])
    fun subscribe(@AuthenticationPrincipal userId: Long): SseEmitter {
        return notificationService.subscribe(userId)
    }

    @GetMapping("/unread/count")
    @ResponseStatus(HttpStatus.OK)
    fun getUnreadNotificationCount(@AuthenticationPrincipal userId: Long): UnreadNotificationCountDTO {
        return notificationService.getUnreadNotificationCount(userId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getNotifications(
        @AuthenticationPrincipal userId: Long,
        @RequestParam page: Int = 1,
    ): List<NotificationDTO> {

        val pageable = PageRequest.of(page - 1, 10)
        return notificationService.getAllNotifications(userId, pageable)
    }

    @PostMapping("/{notificationId}/read")
    @ResponseStatus(HttpStatus.OK)
    fun readNotification(
        @PathVariable notificationId: Long
    ) {
        notificationService.read(notificationId)
    }

    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteNotification(
        @PathVariable notificationId: Long
    ) {
        notificationService.delete(notificationId)
    }
}
