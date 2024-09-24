package zunza.zunlog.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import zunza.zunlog.dto.CommentDTO
import zunza.zunlog.model.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    @Query("SELECT new zunza.zunlog.dto.CommentDTO(c.id, c.content, u.nickname, c.createdDt) " +
        "FROM Comment c " +
        "LEFT JOIN c.user u " +
        "WHERE c.post.id = :postId")
    fun findByPostIdWithPaging(@Param("postId") postId: Long, pageable: Pageable): Page<CommentDTO>
}