package zunza.zunlog.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.repository.SubscriptionRepository
import zunza.zunlog.service.NotificationService

@Component
class CustomEventListener(
    private val notificationService: NotificationService,
) {

    @EventListener
    fun notifyNewPost(postEvent: PostEvent) {
        notificationService.notify(postEvent)
    }
}
