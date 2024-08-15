package zunza.zunlog.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.repository.UserRepository
import zunza.zunlog.config.UserDetails as CustomUserDetails

@Service
class UserDetailsService(private val userRepository: UserRepository):
    UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails {
        if (email.isNullOrEmpty()) {
            throw UserNotFoundException()
        }
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()

        return CustomUserDetails(user)
    }
}
