package zunza.zunlog.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.model.Post
import zunza.zunlog.model.QPost

@Repository
interface PostRepository: JpaRepository<Post, Long>, PostRepositoryCustom {
}

interface  PostRepositoryCustom {
    fun findPostByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO>
}

class PostRepositoryCustomImpl : PostRepositoryCustom, QuerydslRepositorySupport(Post::class.java) {
    private val post = QPost.post
    override fun findPostByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        val builder = BooleanBuilder()

        when (condition) {
            "title" -> builder.and(post.title.contains(value))
            "writer" -> builder.and(post.writer.eq(value))
        }

        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.writer,
                    post.createdDt,
                    post.updatedDt
                )
            )
            .where(builder)
            .orderBy(post.createdDt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}


