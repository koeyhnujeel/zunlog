package zunza.zunlog.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import zunza.zunlog.dto.CreateUserDTO
import zunza.zunlog.dto.EmailDuplicationDTO
import zunza.zunlog.dto.NicknameDuplicationDTO
import zunza.zunlog.service.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody createUserDTO: CreateUserDTO) {
        userService.join(createUserDTO)
    }

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    fun checkEmailDuplication(@RequestParam email: String): EmailDuplicationDTO {
        return userService.isEmailDuplicated(email)
    }

    @GetMapping("/check-nickname")
    @ResponseStatus(HttpStatus.OK)
    fun checkNicknameDuplication(@RequestParam nickname: String): NicknameDuplicationDTO {
        return userService.isNicknameDuplicated(nickname)
    }
}