package zunza.zunlog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.dto.NotificationDTO
import zunza.zunlog.dto.UnreadNotificationCountDTO
import zunza.zunlog.event.CustomEvent
import zunza.zunlog.exception.NotificationNotFoundException
import zunza.zunlog.model.Notification
import zunza.zunlog.model.Subscription
import zunza.zunlog.repository.NotificationRepository
import zunza.zunlog.repository.SubscriptionRepository
import zunza.zunlog.repository.SseEmitterRepository
import java.io.IOException

@Service
class NotificationService(
    private val sseEmitterRepository: SseEmitterRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val notificationRepository: NotificationRepository,
) {

    fun subscribe(userId: Long): SseEmitter {
        val emitter = SseEmitter(60L * 1000 * 60)
        sseEmitterRepository.add(userId, emitter)
        emitter.send(SseEmitter.event()
            .name("connect")
            .data("connected")
        )
        return emitter
    }

    fun notify(event: CustomEvent) {
        val subscriptions = subscriptionRepository.findByTargetId(event.senderId)
        val subscribers = findConnectedSubscribers(subscriptions)

        val notifications = mutableListOf<Notification>()
        subscribers.forEach { (subscriberId, emitter)  ->
            val notification = Notification(senderId = event.senderId, receiverId = subscriberId, message = event.getMessage())
            notifications.add(notification)
            try {
                emitter.send(
                    SseEmitter.event()
                        .name(event.getEventName())
                        .data(event.getMessage())
                )
            } catch (e: IOException) {
                emitter.complete()
                sseEmitterRepository.deleteByUserId(subscriberId)
            }
        }
        notificationRepository.saveAll(notifications)
    }

    private fun findConnectedSubscribers(subscriptions: List<Subscription>): Map<Long, SseEmitter> {
        return subscriptions.mapNotNull {subscription ->
            val emitter = sseEmitterRepository.findBySubscriberId(subscription.subscriberId)
            emitter?.let { subscription.subscriberId to emitter }
        }.toMap()
    }

    fun getAllNotifications(userId: Long, pageable: Pageable): Page<NotificationDTO> {
        return notificationRepository.findAll(pageable)
            .map { NotificationDTO.from(it) }
    }

    fun getUnreadNotificationCount(userId: Long): UnreadNotificationCountDTO {
        val count = notificationRepository.countUnreadNotifications(userId)
        return UnreadNotificationCountDTO(count)
    }

    @Transactional
    fun read(notificationId: Long) {
        val notification = notificationRepository.findById(notificationId).orElseThrow {
            throw NotificationNotFoundException()
        }
        notification.UpdateStatus()
    }
}
