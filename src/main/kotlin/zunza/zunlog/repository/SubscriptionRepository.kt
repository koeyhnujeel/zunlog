package zunza.zunlog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Subscription

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findByTargetIdAndSubscriberId(targetId: Long, subscriberId: Long): Subscription

    fun findByTargetId(targetId: Long): List<Subscription>
}