package zunza.zunlog.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreateUserDTO
import zunza.zunlog.model.User
import zunza.zunlog.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun join(createUserDTO: CreateUserDTO) {
        val encodedPassword = passwordEncoder.encode(createUserDTO.password)
        val user = User.of(createUserDTO.email, encodedPassword, createUserDTO.nickname)
        userRepository.save(user)
    }
}