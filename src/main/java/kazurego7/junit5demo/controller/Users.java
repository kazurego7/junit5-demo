package kazurego7.junit5demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class Users {
    @PostMapping()
    public CreateUserResponse createUser(@RequestBody
    CreateUserRequestBody createUserRequestBody) {
        return null;
    }
}
