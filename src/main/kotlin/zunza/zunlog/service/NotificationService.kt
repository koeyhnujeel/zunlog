package zunza.zunlog.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.dto.NotificationDTO
import zunza.zunlog.dto.UnreadNotificationCountDTO
import zunza.zunlog.event.CustomEvent
import zunza.zunlog.exception.NotificationNotFoundException
import zunza.zunlog.model.Notification
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

        val notifications = subscriptions.stream()
            .map { Notification(senderId = event.senderId, receiverId = it.subscriberId, message = event.getMessage()) }
            .toList()
        notificationRepository.saveAll(notifications)

        subscriptions.forEach { subscription ->
            sseEmitterRepository.findBySubscriberId(subscription.subscriberId)?.let {
                try {
                    it.send(
                        SseEmitter.event()
                            .name(event.getEventName())
                            .data(event.getMessage())
                    )
                } catch (e: IOException) {
                    it.complete()
                    sseEmitterRepository.deleteByUserId(subscription.subscriberId)
                }
            }
        }
    }

    fun getAllNotifications(userId: Long, pageable: Pageable): List<NotificationDTO> {
        return notificationRepository.findByReceiverId(userId, pageable)
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
        notification.updateStatus()
    }

    fun delete(notificationId: Long) {
        notificationRepository.deleteById(notificationId)
    }
}
