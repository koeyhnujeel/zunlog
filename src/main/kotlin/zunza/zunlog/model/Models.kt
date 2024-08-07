package zunza.zunlog.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var title: String,
    var content: String,
    val writer: String,
    var createdDt: Instant,
    var updatedDt: Instant,
)
