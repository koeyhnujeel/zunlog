package zunza.zunlog.service

import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreateSubscriptionDTO
import zunza.zunlog.exception.SelfSubscriptionException
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.Subscription
import zunza.zunlog.repository.SubscribeRepository
import zunza.zunlog.repository.UserRepository

@Service
class SubscribeService(
    private val subscribeRepository: SubscribeRepository,
    private val userRepository: UserRepository,
) {
    fun subscribe(createSubscriptionDTO: CreateSubscriptionDTO) {
        val targetUser = userRepository.findById(createSubscriptionDTO.targetId).orElseThrow() {
            throw UserNotFoundException()
        }

        if (targetUser.id == createSubscriptionDTO.subscriberId) {
            throw SelfSubscriptionException()
        }

        val subscription = Subscription.of(targetUser.id, createSubscriptionDTO.subscriberId)
        subscribeRepository.save(subscription)
    }
}
