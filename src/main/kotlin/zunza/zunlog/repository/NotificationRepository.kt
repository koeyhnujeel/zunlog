package zunza.zunlog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Notification

@Repository
interface NotificationRepository: JpaRepository<Notification, Long> {

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.receiverId = :id " +
            "AND n.isRead = 'FALSE'")
    fun countUnreadNotifications(@Param("id") id: Long): Int
}
