package zunza.zunlog.repository

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Post
import zunza.zunlog.model.QPost
import zunza.zunlog.util.FullTextSearch

@Repository
class PostCacheRepository : QuerydslRepositorySupport(Post::class.java) {
    private val post= QPost.post

    @Cacheable(cacheNames = ["searchedPosts"], key = "#value")
    fun countSearchedPosts(value: String): Long {
        val total = from(post)
            .select(post.id)
            .where(FullTextSearch.match(post.title, value))
            .fetchCount()
        return total
    }
}