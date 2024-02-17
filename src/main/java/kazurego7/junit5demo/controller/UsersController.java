package kazurego7.junit5demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kazurego7.junit5demo.usecase.UserUsecase;
import kazurego7.junit5demo.usecase.model.CreateUserInput;
import kazurego7.junit5demo.usecase.model.CreateUserOutput;
import kazurego7.junit5demo.usecase.model.CreateUserOutput.DuplicatedUser;
import kazurego7.junit5demo.usecase.model.CreateUserOutput.NotFoundCompany;
import kazurego7.junit5demo.usecase.model.CreateUserOutput.Success;
import kazurego7.junit5demo.usecase.model.GetUserOutput;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserUsecase userUsecase;

    /**
     * ユーザー登録
     * 
     * @param input
     * @return
     */
    @PostMapping()
    public ResponseEntity<CreateUserOutput> createUser(
            @Valid
            @RequestBody
            CreateUserInput input) {
        var output = userUsecase.createUser(input);;
        ResponseEntity<CreateUserOutput> response = switch (output) {
            case Success() -> new ResponseEntity<>(HttpStatus.ACCEPTED);
            case DuplicatedUser e -> new ResponseEntity<>(e, HttpStatus.CONFLICT);
            case NotFoundCompany e -> new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        };
        return response;
    }

    /**
     * ユーザー詳細取得
     * 
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public GetUserOutput getUser(
            @Valid
            @PathVariable("userId")
            @NotNull
            String userId) {

        var response = userUsecase.getUser(userId);

        // ユーザーが存在しない場合は 404 エラーを返す
        return response.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}
