package zunza.zunlog.service

import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import zunza.zunlog.model.Subscription
import zunza.zunlog.repository.SubscriptionRepository
import zunza.zunlog.repository.SseEmitterRepository
import java.io.IOException

@Service
class NotificationService(
    private val sseEmitterRepository: SseEmitterRepository,
    private val subscriptionRepository: SubscriptionRepository,
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

    fun notify(publisherId: Long) {
        val subscriptions = subscriptionRepository.findByTargetId(publisherId)
        val subscribers = findConnectedSubscribers(subscriptions)

        subscribers.forEach { (subscriberId, emitter)  ->
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("new post")
                        .data("${publisherId}님이 새 글을 등록했습니다!")
                )
            } catch (e: IOException) {
                emitter.complete()
                sseEmitterRepository.deleteByUserId(subscriberId)
            }
        }
    }

    private fun findConnectedSubscribers(subscriptions: List<Subscription>): Map<Long, SseEmitter> {
        return subscriptions.mapNotNull {subscription ->
            val emitter = sseEmitterRepository.findBySubscriberId(subscription.subscriberId)
            emitter?.let { subscription.subscriberId to emitter }
        }.toMap()
    }
}
