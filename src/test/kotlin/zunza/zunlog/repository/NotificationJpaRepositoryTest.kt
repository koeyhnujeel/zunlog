package zunza.zunlog.repository

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import zunza.zunlog.config.QueryDslConfig
import zunza.zunlog.model.Notification
import kotlin.test.Test

@DataJpaTest
@Import(QueryDslConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationJpaRepositoryTest {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Test
    fun `알림_데이터_saveAll()_jpa`() {

        // given
        val notifications = mutableListOf<Notification>()
        for (i in 2..100001) {
            val notification = Notification(senderId = 1L, referenceId = i.toLong(), receiverId = 1L, message = "TEST$i")
            notifications.add(notification)
        }

        //when
        notificationRepository.saveAll(notifications)

        //then
        val count = notificationRepository.count()
        assertEquals(100000, count)
    }
}
