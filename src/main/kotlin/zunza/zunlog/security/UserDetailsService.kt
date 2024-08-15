package zunza.zunlog.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService
import zunza.zunlog.exception.UserNotFoundException
import zunza.zunlog.repository.UserRepository

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails {
        if (email.isNullOrEmpty()) {
            throw UserNotFoundException()
        }
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()

        return zunza.zunlog.security.CustomUserDetails(user)
    }
}
