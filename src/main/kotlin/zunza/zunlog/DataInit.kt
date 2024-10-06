//package zunza.zunlog
//
//import net.datafaker.Faker
//import org.springframework.boot.CommandLineRunner
//import org.springframework.jdbc.core.BatchPreparedStatementSetter
//import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Component
//import java.sql.PreparedStatement
//import java.time.Instant
//import java.util.Locale
//
//@Component
//class DataInit(
//    private val passwordEncoder: PasswordEncoder,
//    private val jdbcTemplate: JdbcTemplate,
//) : CommandLineRunner {
//    override fun run(vararg args: String?) {
//
//        val koFaker = Faker(Locale("ko"))
//        val enFaker = Faker()
//
//        val users = List(100000) { 0 }
//        batchInsertUsers(users, enFaker)
//
//        val posts = List(1000000) { 0 }
//        batchInsertPosts(posts, koFaker)
//
//        batchInsertLike()
//        batchInsertComment()
//
//        println("================Insert Complete====================")
//    }
//
//    private fun batchInsertUsers(users: List<Int>, faker: Faker) {
//        val sql = "INSERT INTO user (email, password, nickname) VALUES (?, ?, ?)"
//
//        val batchSize = 50000
//        for (i in users.indices step batchSize) {
//            val end = Math.min(i + batchSize, users.size)
//            val batchList = users.subList(i, end)
//
//            jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
//                override fun setValues(ps: PreparedStatement, index: Int) {
//                    val userIndex = batchList[index]
//                    val nickname = faker.name().firstName() + userIndex
//                    val email = "${nickname}@email.com"
//                    val password = passwordEncoder.encode("1234")
//
//                    ps.setString(1, email)
//                    ps.setString(2, password)
//                    ps.setString(3, nickname)
//                }
//
//                override fun getBatchSize(): Int {
//                    return batchList.size
//                }
//            })
//        }
//    }
//
//    private fun batchInsertPosts(posts: List<Int>, faker: Faker) {
//        val sql = "INSERT INTO post (title, content, summary, user_id, created_dt, updated_dt) " +
//            "VALUES (?, ?, ?, ?, ?, ?)"
//
//        var time: Long = 1L
//        val batchSize = 200000
//        for (i in posts.indices step batchSize) {
//            val end = Math.min(i + batchSize, posts.size)
//            val batchList = posts.subList(i, end)
//
//            jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
//                override fun setValues(ps: PreparedStatement, index: Int) {
//                    val post = batchList[index]
//                    ps.setString(1, generateBlogTitle(faker))
//                    ps.setString(2, generateBlogTitle(faker))
//                    ps.setString(3, generateBlogTitle(faker))
//                    ps.setLong(4, (1 .. 100000).random().toLong())
//                    ps.setTimestamp(5, java.sql.Timestamp.from(Instant.now().plusMillis(time)))
//                    ps.setTimestamp(6, java.sql.Timestamp.from(Instant.now().plusMillis(time++)))
//                }
//
//                override fun getBatchSize(): Int {
//                    return batchList.size
//                }
//            })
//        }
//    }
//
//    private fun batchInsertLike() {
//        val sql = "INSERT INTO post_like (user_id, post_id, created_dt) " +
//            "VALUES (?, ?, ?)"
//
//        var time = 1L
//        for (i in 1..1000000) {
//            jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
//                override fun setValues(ps: PreparedStatement, index: Int) {
//                    ps.setLong(1, (1..100000).random().toLong())
//                    ps.setLong(2, i.toLong())
//                    ps.setTimestamp(3, java.sql.Timestamp.from(Instant.now().plusMillis(time++)))
//                }
//
//                override fun getBatchSize(): Int {
//                    return 50
//                }
//            })
//        }
//    }
//
//    private fun batchInsertComment() {
//        val sql = "INSERT INTO comment (content, user_id, post_id, created_dt, updated_dt) " +
//            "VALUES (?, ?, ?, ?, ?)"
//
//        var time = 1L
//        for (i in 1..1000000) {
//            jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
//                override fun setValues(ps: PreparedStatement, index: Int) {
//                    ps.setString(1, "댓글입니다. $i")
//                    ps.setLong(2, (1..100000).random().toLong())
//                    ps.setLong(3, i.toLong())
//                    ps.setTimestamp(4, java.sql.Timestamp.from(Instant.now().plusMillis(time)))
//                    ps.setTimestamp(5, java.sql.Timestamp.from(Instant.now().plusMillis(time++)))
//                }
//
//                override fun getBatchSize(): Int {
//                    return 50
//                }
//            })
//
//        }
//    }
//
//    private fun generateBlogTitle(faker: Faker): String {
//        val templates = listOf(
//            { "${faker.lorem().word()} ${faker.verb().base()}는 방법" },
//            { "${faker.programmingLanguage().name()}로 ${faker.verb().base()}기" },
//            { "${faker.animal().name()}에 대한 ${faker.number().numberBetween(3, 10)}가지 ${faker.lorem().words()}" },
//            { "${faker.food().dish()}로 배우는 ${faker.job().title()}" },
//            { "${faker.book().title()} 책 리뷰: ${faker.lorem().word()} 독후감" },
//            { "${faker.nation().nationality()}의 ${faker.color().name()} ${faker.animal().name()}" },
//            { "${faker.artist().name()}의 ${faker.music().instrument()}로 보는 ${faker.verb().ingForm()}" },
//            { "${faker.rockBand().name()} ${faker.music().genre()} 앨범 분석" },
//            { "${faker.ancient().god()}과 ${faker.ancient().primordial()}의 ${faker.lorem().word()} 이야기" },
//            { "${faker.company().industry()}에서 ${faker.verb().base()}는 ${faker.number().numberBetween(5, 15)}가지 방법" },
//            { "${faker.weather().description()}한 날 ${faker.verb().base()}는 ${faker.lorem().word()}" },
//            { "${faker.lordOfTheRings().character()}에게 배우는 ${faker.job().position()} 성공 비결" },
//            { "${faker.pokemon().name()}과 함께하는 ${faker.esports().event()} 대회" },
//            { "${faker.company().name()}의 새로운 ${faker.commerce().productName()}: ${faker.marketing().buzzwords()}" }
//        )
//
//        return templates.random().invoke()
//    }
//}