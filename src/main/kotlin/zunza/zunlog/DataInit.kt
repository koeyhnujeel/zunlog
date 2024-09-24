package zunza.zunlog

import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.model.*
import zunza.zunlog.repository.PostRepository
import zunza.zunlog.repository.SubscriptionRepository
import zunza.zunlog.repository.UserRepository
import java.sql.PreparedStatement

@Component
class DataInit(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jdbcTemplate: JdbcTemplate,
    private val subscriptionRepository: SubscriptionRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val users = mutableListOf<User>()
        for (i in 1..100) {
            val user =
                User.of(email = "user$i@email.com", password = passwordEncoder.encode("1234"), nickname = "user$i")
            userRepository.save(user)
        }
        userRepository.saveAll(users)

        val subscriptions = mutableListOf<Subscription>()
        for (i in 2..101) {
            val subscription = Subscription.of(targetId = 1L, subscriberId = i.toLong())
            subscriptions.add(subscription)
        }
        subscriptionRepository.saveAll(subscriptions)

        val user = userRepository.findById(1L).orElseThrow { UserNotFoundException() }
        val posts = mutableListOf<Post>()
        for (i in 1..1000000) {
            val post = Post.of(title = "title$i", content = "content$i", summary = "summary$i", user = user)
            posts.add(post)
        }
        batchInsertPosts(posts)
        println("================Insert Complete====================")
    }

    private fun batchInsertPosts(posts: List<Post>) {
        val sql = "INSERT INTO post (title, content, summary, user_id, created_dt, updated_dt) " +
            "VALUES (?, ?, ?, ?, ?, ?)"

        val batchSize = 200000
        for (i in posts.indices step batchSize) {
            val end = Math.min(i + batchSize, posts.size)
            val batchList = posts.subList(i, end)

            jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, index: Int) {
                    val post = batchList[index]
                    ps.setString(1, post.title)
                    ps.setString(2, post.content)
                    ps.setString(3, post.summary)
                    ps.setLong(4, post.user.id)
                    ps.setTimestamp(5, java.sql.Timestamp.from(post.createdDt))
                    ps.setTimestamp(6, java.sql.Timestamp.from(post.updatedDt))
                }

                override fun getBatchSize(): Int {
                    return batchList.size
                }
            })
        }
    }
}