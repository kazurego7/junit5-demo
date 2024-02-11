package kazurego7.junit5demo.usecase;

import java.util.Optional;
import org.springframework.stereotype.Service;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.domain.valueObject.UserType;
import kazurego7.junit5demo.usecase.model.CreateUserInput;
import kazurego7.junit5demo.usecase.model.GetUserOutput;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserUsecase {

    private final UserRepository userRepository;

    public void createUser(CreateUserInput input) {
        var user = new UserEntity();
        user.setUserId(input.getUserId());
        user.setUserName(input.getUserName());
        user.setMailAddress(input.getMailAddress());
        user.setUserType(UserType.GENERAL);
        userRepository.save(user);
    }

    public Optional<GetUserOutput> getUser(String userId) {
        if (userId == null) {
            return Optional.empty();
        }
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        var user = optionalUser.get();
        var response =
                new GetUserOutput(user.getUserId(), user.getUserName(), user.getMailAddress(),
                        user.getUserType().getValue());
        return Optional.of(response);
    }
}
