package kazurego7.junit5demo.usecase;

import java.util.Optional;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.repository.CompanyRepository;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.domain.valueObject.UserType;
import kazurego7.junit5demo.usecase.model.CreateUserInput;
import kazurego7.junit5demo.usecase.model.CreateUserOutput;
import kazurego7.junit5demo.usecase.model.GetUserOutput;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserUsecase {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public CreateUserOutput createUser(CreateUserInput input) {
        if (userRepository.existsById(input.getUserId())) {
            return new CreateUserOutput.DuplicatedUser("User already exists");
        }
        var optCompany = companyRepository.findById(input.getCompanyId());
        if (optCompany.isEmpty()) {
            return new CreateUserOutput.NotFoundCompany("Company not found");
        }
        var company = optCompany.get();

        var user = new UserEntity();
        user.setUserId(input.getUserId());
        user.setUserName(input.getUserName());
        user.setMailAddress(input.getMailAddress());
        user.setUserType(UserType.GENERAL);
        user.setCompany(company);
        userRepository.save(user);

        return new CreateUserOutput.Success();
    }

    public Optional<GetUserOutput> getUser(
            @NotNull
            String userId) {
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
