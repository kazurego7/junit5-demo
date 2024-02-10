package kazurego7.junit5demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kazurego7.junit5demo.controller.model.CreateUserRequestBody;
import kazurego7.junit5demo.controller.model.GetUserResponse;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.domain.valueObject.UserType;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public void createUser(
            @Valid
            @RequestBody
            CreateUserRequestBody requestBody) {
        var user = new UserEntity();
        user.setUserId(requestBody.getUserId());
        user.setUserName(requestBody.getUserName());
        user.setMailAddress(requestBody.getMailAddress());
        user.setUserType(UserType.GENERAL);
        userRepository.save(user);
        return;
    }

    @GetMapping("/{userId}")
    public GetUserResponse getUser(
            @Valid
            @PathVariable("userId")
            @NotNull
            String userId) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        var user = optionalUser.get();
        var response =
                new GetUserResponse(user.getUserId(), user.getUserName(), user.getMailAddress(),
                        user.getUserType().getValue());
        return response;
    }

}
