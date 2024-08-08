package zunza.zunlog

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import zunza.zunlog.model.Post
import zunza.zunlog.repository.PostRepository

@Component
class DataInit(
    private val postRepository: PostRepository
): CommandLineRunner {
    override fun run(vararg args: String?) {
        val posts = mutableListOf<Post>()

        for (i in 1..20) {
            val post = Post(title = "title$i", content = "content$i", writer = "writer$i")
            posts.add(post)
        }
        postRepository.saveAll(posts)
    }
}
