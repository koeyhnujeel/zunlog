package zunza.zunlog.repository

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Component
class SseEmitterRepository {

    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    fun add(userId: Long, emitter: SseEmitter) {
        this.emitters[userId] = emitter
        emitter.onCompletion { this.emitters.remove(userId) }
        emitter.onTimeout { this.emitters.remove(userId) }
    }

    fun deleteByUserId(userId: Long) {
        emitters.remove(userId)
    }

    fun findBySubscriberId(subscriberId: Long): SseEmitter? {
        return emitters[subscriberId]
    }
}
