package zunza.zunlog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import zunza.zunlog.model.PostLike

@Repository
interface LikeRepository : JpaRepository<PostLike, Long> {

    @Modifying
    @Query(
        "DELETE FROM PostLike l " +
            "WHERE l.user.id = :userId " +
            "AND l.post.id = :postId"
    )
    fun deleteByUserIdAndPostId(@Param("userId")userId: Long, @Param("postId")postId: Long)
}