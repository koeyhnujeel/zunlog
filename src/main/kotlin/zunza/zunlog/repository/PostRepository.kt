package zunza.zunlog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zunza.zunlog.model.Post

@Repository
interface PostRepository: JpaRepository<Post, Long> {
}
