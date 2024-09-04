package zunza.zunlog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
}
