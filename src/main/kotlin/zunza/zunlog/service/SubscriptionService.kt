package zunza.zunlog.service

import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreateSubscriptionDTO
import zunza.zunlog.dto.RemoveSubscriptionDTO
import zunza.zunlog.exception.SelfSubscriptionException
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.Subscription
import zunza.zunlog.repository.SubscriptionRepository
import zunza.zunlog.repository.UserRepository

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
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
        subscriptionRepository.save(subscription)
    }

    fun unsubscribe(removeSubscriptionDTO: RemoveSubscriptionDTO) {
        val subscription = subscriptionRepository.findByTargetIdAndSubscriberId(
            removeSubscriptionDTO.targetId,
            removeSubscriptionDTO.subscriberId
        )
        subscriptionRepository.delete(subscription)
    }
}