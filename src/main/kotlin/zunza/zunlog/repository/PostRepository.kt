package zunza.zunlog.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import zunza.zunlog.dto.CommentDTO
import zunza.zunlog.dto.PostDTO
import zunza.zunlog.dto.PostDetailDTO
import zunza.zunlog.model.Post
import zunza.zunlog.model.QComment
import zunza.zunlog.model.QPost
import zunza.zunlog.model.QUser

@Repository
interface PostRepository: JpaRepository<Post, Long>, PostRepositoryCustom {
}

interface  PostRepositoryCustom {
    fun findPostByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO>

    fun findByIdWithUserAndCommentV1(postId: Long): PostDetailDTO?

    fun findByIdWithUserAndCommentV2(postId: Long): PostDetailDTO?
}

class PostRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : PostRepositoryCustom, QuerydslRepositorySupport(Post::class.java) {

    private val post = QPost.post
    private val user = QUser.user
    private val comment = QComment.comment

    override fun findPostByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        val builder = BooleanBuilder()

        when (condition) {
            "title" -> builder.and(post.title.contains(value))
            "writer" -> builder.and(post.user.nickname.eq(value))
        }

        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.user.nickname,
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

    override fun findByIdWithUserAndCommentV1(postId: Long): PostDetailDTO? {
        val postDTO = from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.user.nickname,
                    post.createdDt,
                    post.updatedDt
                )
            )
            .leftJoin(post.user, user)
            .where(post.id.eq(postId))
            .fetchOne() ?: return null

        val commentsDTO = from(comment)
            .select(
                Projections.constructor(
                    CommentDTO::class.java,
                    comment.id,
                    comment.content,
                    comment.user.nickname,
                    comment.createdDt
                )
            )
            .leftJoin(comment.user, user)
            .where(comment.post.id.eq(postId))
            .fetch()

        return PostDetailDTO.of(postDTO, commentsDTO)
    }

    override fun findByIdWithUserAndCommentV2(postId: Long): PostDetailDTO? {
        return jpaQueryFactory.selectFrom(post)
            .leftJoin(post.comments, comment)
            .leftJoin(comment.user, user)
            .where(post.id.eq(postId))
            .transform(GroupBy.groupBy(post.id).list(
                Projections.constructor(
                    PostDetailDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.user.nickname,
                    post.createdDt,
                    post.updatedDt,
                    GroupBy.list(
                        Projections.constructor(
                            CommentDTO::class.java,
                            comment.id,
                            comment.content,
                            comment.user.nickname,
                            comment.createdDt
                        ).skipNulls()
                    )
                )
            )
        ).firstOrNull()
    }
}
