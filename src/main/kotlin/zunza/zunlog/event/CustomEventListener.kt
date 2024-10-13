package zunza.zunlog.event

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import zunza.zunlog.service.NotificationService

@Component
class CustomEventListener(
    private val notificationService: NotificationService,
) {

    @Async("eventExecutor")
    @EventListener
    fun notifyHandler(customEvent: CustomEvent) {
        when (customEvent.getType()) {
            "post" -> notificationService.postNotify(customEvent)
            "comment" -> notificationService.commentAndLikeNotify(customEvent)
            "like" -> notificationService.commentAndLikeNotify(customEvent)
        }
    }
}