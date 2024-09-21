package zunza.zunlog.repository

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Notification
import java.sql.PreparedStatement

@Repository
class NotificationJdbcRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun batchInsertNotifications(notifications: List<Notification>) {

        val sql = "INSERT INTO notification (sender_id, receiver_id, reference_id, message, is_read, created_dt) " +
            "VALUES (?, ?, ?, ?, ?, ?)"

        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val notification = notifications[i]
                ps.setLong(1, notification.senderId)
                ps.setLong(2, notification.receiverId)
                ps.setLong(3, notification.referenceId)
                ps.setString(4, notification.message)
                ps.setString(5, notification.isRead.name)
                ps.setTimestamp(6, java.sql.Timestamp.from(notification.createdDt))
            }

            override fun getBatchSize(): Int {
                return notifications.size
            }
        })
    }
}