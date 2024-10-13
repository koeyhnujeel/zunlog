package zunza.zunlog.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import zunza.zunlog.Enum.IsLike
import zunza.zunlog.dto.*
import zunza.zunlog.model.Post
import zunza.zunlog.model.QComment
import zunza.zunlog.model.QPost
import zunza.zunlog.model.QUser
import zunza.zunlog.scheduler.PageInfo
import zunza.zunlog.util.FullTextSearch
import kotlin.math.absoluteValue

@Repository
interface PostRepository : JpaRepository<Post, Long>, PostRepositoryCustom

interface PostRepositoryCustom {
    fun findPostList(pageable: Pageable): Page<PostListDTO>
    fun findPostByCondition(condition: String, value: String, pageDTO: PageDTO): Page<PostListDTO>
    fun findByIdWithUserAndCommentV1(postId: Long): PostDetailDTOv1?
    fun findByIdWithUserAndCommentV2(userId: Long, postId: Long): PostDetailDTOv2?
    fun findPostListWithNoOffset(pageDTO: PageDTO): Page<PostListDTO>
}

class PostRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val commentRepository: CommentRepository,
    private val likeRepository: LikeRepository,
    private val pageInfo: PageInfo
) : PostRepositoryCustom, QuerydslRepositorySupport(Post::class.java) {

    private val post = QPost.post
    private val user = QUser.user
    private val comment = QComment.comment

    override fun findPostList(pageable: Pageable): Page<PostListDTO> {
        val queryResult = from(post)
            .select(
                Projections.constructor(
                    PostListDTO::class.java,
                    post.id,
                    post.title,
                    post.summary,
                    user.nickname,
                    post.likes.size(),
                    post.comments.size(),
                    post.createdDt
                )
            )
            .leftJoin(post.user, user)
            .orderBy(post.id.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetchResults()

        val content = queryResult.results
        val total = queryResult.total

        return PageImpl(content, pageable, total)
    }

    override fun findPostListWithNoOffset(pageDTO: PageDTO): Page<PostListDTO> {
        val builder = BooleanBuilder()
        buildKeySetPaginationPredicate(pageDTO, builder)

        val content = from(post)
            .select(
                Projections.constructor(
                    PostListDTO::class.java,
                    post.id,
                    post.title,
                    post.summary,
                    user.nickname,
                    post.likes.size(),
                    post.comments.size(),
                    post.createdDt
                )
            )
            .leftJoin(post.user, user)
            .where(builder)
            .orderBy(post.id.desc())
            .limit(pageDTO.size)
            .fetch()

        val pageable = PageRequest.of(pageDTO.targetPage, pageDTO.size.toInt())
        val total = pageInfo.totalElements

        return PageImpl(content, pageable, total)
    }

    override fun findPostByCondition(condition: String, value: String, pageDTO: PageDTO): Page<PostListDTO> {
        val builder = BooleanBuilder()

        when (condition) {
            "title" -> builder.and(FullTextSearch.match(post.title, value))
//            "title" -> builder.and(post.title.contains(value))
            "writer" -> builder.and(post.user.nickname.eq(value))
        }

        buildKeySetPaginationPredicate(pageDTO, builder)

        val content = from(post)
            .select(
                Projections.constructor(
                    PostListDTO::class.java,
                    post.id,
                    post.title,
                    post.summary,
                    user.nickname,
                    post.likes.size(),
                    post.comments.size(),
                    post.createdDt
                )
            )
            .leftJoin(post.user, user)
            .where(builder)
            .orderBy(post.id.desc())
            .limit(pageDTO.size)
            .fetch()

        val pageable = PageRequest.of(pageDTO.targetPage, pageDTO.size.toInt())
        val total = pageInfo.totalElements

        return PageImpl(content, pageable, total)
    }

    private fun buildKeySetPaginationPredicate(pageDTO: PageDTO, builder: BooleanBuilder) {
        val interval = (pageDTO.currentPage - pageDTO.targetPage).absoluteValue

        with(builder) {
            if (pageDTO.lastPostId != 0) {
                if (pageDTO.targetPage > pageDTO.currentPage) {
                    and(post.id.lt(pageDTO.lastPostId - (interval - 1) * pageDTO.size))
                } else {
                    and(post.id.lt(pageDTO.lastPostId + (interval + 1) * pageDTO.size))
                }
            }
        }
    }

    override fun findByIdWithUserAndCommentV1(postId: Long): PostDetailDTOv1? {
        return jpaQueryFactory.selectFrom(post)
            .leftJoin(post.comments, comment)
            .leftJoin(comment.user, user)
            .where(post.id.eq(postId))
            .transform(
                GroupBy.groupBy(post.id).list(
                    Projections.constructor(
                        PostDetailDTOv1::class.java,
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
                        ),
                        post.likes.size()
                    )
                )
            ).firstOrNull()
    }

    override fun findByIdWithUserAndCommentV2(userId: Long, postId: Long): PostDetailDTOv2? {
        val postDTO = from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.user.nickname,
                    post.likes.size(),
                    post.createdDt,
                    post.updatedDt
                )
            )
            .leftJoin(post.user, user)
            .where(post.id.eq(postId))
            .fetchOne() ?: return null

        val pageable = PageRequest.of(0, 20)
        val commentsDTO = commentRepository.findByPostIdWithPaging(postId, pageable)
        val isLike = when(likeRepository.existsByUserIdAndPostId(userId, postId)) {
            true -> IsLike.TRUE
            false -> IsLike.FALSE
        }

        return PostDetailDTOv2(
            postDTO.id,
            postDTO.title,
            postDTO.content,
            postDTO.writer,
            postDTO.createdDt,
            postDTO.updatedDt,
            postDTO.likeCount,
            isLike,
            commentsDTO.totalPages,
            commentsDTO.content
        )
    }
}