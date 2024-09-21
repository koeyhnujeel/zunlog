package zunza.zunlog.repository

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.ComponentScan
import org.springframework.jdbc.core.JdbcTemplate
import zunza.zunlog.model.Notification
import kotlin.test.Test

@DataJdbcTest
@ComponentScan(basePackages = ["zunza.zunlog.repository"])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationJdbcRepositoryTest {

    @Autowired
    private lateinit var notificationJdbcRepository: NotificationJdbcRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `알림_데이터_Batch_Insert_jdbc`() {

        // given
        val notifications = mutableListOf<Notification>()
        for (i in 2..100001) {
            val notification = Notification(senderId = 1L, referenceId = i.toLong(), receiverId = 1L, message = "TEST$i")
            notifications.add(notification)
        }

        //when
        notificationJdbcRepository.batchInsertNotifications(notifications)

        //then
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM notification", Int::class.java)
        assertEquals(100000, count)
    }
}
