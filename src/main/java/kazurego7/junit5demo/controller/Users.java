package kazurego7.junit5demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.value.UserType;
import kazurego7.junit5demo.repository.UserRepository;



@RestController
@RequestMapping("/users")
public class Users {

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public void createUser(@RequestBody
    CreateUserRequestBody createUserRequestBody) {
        var user = new UserEntity();
        user.setUserId(createUserRequestBody.userId());
        user.setUserName(createUserRequestBody.userName());
        user.setMailAddress(createUserRequestBody.mailAddress());
        user.setUserType(UserType.GENERAL);
        userRepository.save(user);
        return;
    }

    @GetMapping("/{userId}")
    public GetUserResponse getMethodName(@Validated
    @PathVariable("userId")
    @NotNull
    String userId) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        var user = optionalUser.get();
        var res = new GetUserResponse(user.getUserId(), user.getUserName(), user.getMailAddress(),
                user.getUserType().getValue());
        return res;
    }

}
