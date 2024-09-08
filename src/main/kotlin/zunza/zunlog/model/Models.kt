package zunza.zunlog.model

import jakarta.persistence.*
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import zunza.zunlog.Enum.IsRead
import zunza.zunlog.dto.UpdatePostDTO
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
class Post private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    title: String,
    content: String,
    viewCount: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToMany(mappedBy = "post")
    val comments: List<Comment> = emptyList(),

    val createdDt: Instant = Instant.now(),
    @LastModifiedDate
    var updatedDt: Instant = Instant.now(),
) {

    var title = title
        protected set

    var content = content
        protected set

    var viewCount = viewCount
        protected set

    fun update(updatePostDTO: UpdatePostDTO) {
        this.title = updatePostDTO.title
        this.content = updatePostDTO.content
    }

    companion object {
        fun of(user: User, title: String, content: String): Post {
            return Post(
                title = title,
                content = content,
                user = user
            )
        }
    }
}

@Entity
@EntityListeners(AuditingEntityListener::class)
class User private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val email: String,
    password: String,
    val nickname: String,
    val role: String = "ROLE_USER",
    val createdDt: Instant = Instant.now(),
    @LastModifiedDate
    var updatedDt: Instant = Instant.now()
) {

    var password = password
        protected set
    companion object {
        fun of(email: String, password: String, nickname: String): User {
            return User(
                email = email,
                password = password,
                nickname = nickname,
            )
        }
    }
}

@Entity
class Subscription private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val targetId: Long,
    val subscriberId: Long,
    val createdDt: Instant = Instant.now()
) {
    companion object {
        fun of(targetId: Long, subscriberId: Long): Subscription {
            return Subscription(targetId = targetId, subscriberId = subscriberId)
        }
    }
}

@Entity
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val senderId: Long,
    val receiverId: Long,
    val message: String,
    isRead: IsRead = IsRead.FALSE,
    val createdDt: Instant = Instant.now()
) {

    @Enumerated(EnumType.STRING)
    var isRead = isRead
        protected set

    fun updateStatus() {
        this.isRead = IsRead.TRUE
    }
}

@Entity
@EntityListeners(AuditingEntityListener::class)
class Comment private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    val createdDt: Instant = Instant.now(),
    @LastModifiedDate
    var updatedDt: Instant = Instant.now()
) {
    var content = content
        protected set

    companion object {
        fun of(content: String, user: User, post: Post): Comment {
            return Comment(
                content = content,
                user = user,
                post = post
            )
        }
    }

    fun updateComment(content: String) {
        this.content = content
    }
}
