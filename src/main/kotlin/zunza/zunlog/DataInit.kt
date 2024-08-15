package zunza.zunlog

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import zunza.zunlog.model.Post
import zunza.zunlog.model.User
import zunza.zunlog.repository.PostRepository
import zunza.zunlog.repository.UserRepository

@Component
class DataInit(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): CommandLineRunner {
    override fun run(vararg args: String?) {
        val posts = mutableListOf<Post>()

        for (i in 1..20) {
            val user = User(email = "user$i@email.com", password = passwordEncoder.encode("1234"), nickname = "user$i")
            userRepository.save(user)
            val post = Post(title = "title$i", content = "content$i", user = user)
            posts.add(post)
        }
        postRepository.saveAll(posts)
    }
}
