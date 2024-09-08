package zunza.zunlog.repository

import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import zunza.zunlog.dto.NotificationDTO
import zunza.zunlog.model.Notification
import zunza.zunlog.model.QNotification

@Repository
interface NotificationRepository : JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    @Query(
        "SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.receiverId = :id " +
            "AND n.isRead = 'FALSE'"
    )
    fun countUnreadNotifications(@Param("id") id: Long): Int
}

interface NotificationRepositoryCustom {

    fun findByReceiverId(id: Long, pageable: Pageable): List<NotificationDTO>
}

class NotificationRepositoryCustomImpl : NotificationRepositoryCustom, QuerydslRepositorySupport(Notification::class.java) {
    private val notification = QNotification.notification

    override fun findByReceiverId(id: Long, pageable: Pageable): List<NotificationDTO> {
        return from(notification)
            .select(
                Projections.constructor(
                    NotificationDTO::class.java,
                    notification.id,
                    notification.message,
                    notification.isRead,
                )
            )
            .where(notification.receiverId.eq(id))
            .orderBy(notification.createdDt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}