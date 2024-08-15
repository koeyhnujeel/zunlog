package zunza.zunlog.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zunza.zunlog.dto.CreateUserDTO
import zunza.zunlog.service.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserDTO: CreateUserDTO) {
        userService.join(createUserDTO)
    }
}
