package zunza.zunlog.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import zunza.zunlog.dto.CreateUserDTO
import zunza.zunlog.dto.EmailDuplicationDTO
import zunza.zunlog.dto.NicknameDuplicationDTO
import zunza.zunlog.exception.EmailDuplicationException
import zunza.zunlog.exception.NicknameDuplicationException
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

    fun isEmailDuplicated(email: String): EmailDuplicationDTO {
        if (userRepository.existsByEmail(email)) {
            throw EmailDuplicationException()
        }
        return EmailDuplicationDTO()
    }

    fun isNicknameDuplicated(nickname: String): NicknameDuplicationDTO {
        if (userRepository.existsByNickname(nickname)) {
            throw NicknameDuplicationException()
        }
        return NicknameDuplicationDTO()
    }
}