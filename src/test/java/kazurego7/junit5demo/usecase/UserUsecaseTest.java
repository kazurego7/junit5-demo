package kazurego7.junit5demo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kazurego7.junit5demo.domain.repository.CompanyRepository;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.usecase.model.CreateUserInput;
import kazurego7.junit5demo.usecase.model.CreateUserOutput;

@ExtendWith(MockitoExtension.class)
public class UserUsecaseTest {

    // モック
    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    // テスト対象
    @InjectMocks
    private UserUsecase userUsecase;

    @Nested
    class ユーザー登録 {

        private CreateUserInput input;

        @BeforeEach
        public void setUp() {
            input = new CreateUserInput("kazurego7", "numnum", "kazurego7@gmail.com",
                    "123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        void ユーザーが登録済みの場合_不正である() {
            // Arrange
            when(userRepository.existsById("kazurego7")).thenReturn(true);

            // Act
            var response = userUsecase.createUser(input);

            // Assert


            assertThat(response)
                    .isExactlyInstanceOf(CreateUserOutput.DuplicatedUser.class)
                    .hasFieldOrPropertyWithValue("errorMessage", "User already exists");
        }

        @Test
        void 会社が存在しない場合_不正である() {
            // Arrange
            when(userRepository.existsById("kazurego7")).thenReturn(false);
            when(companyRepository.findById("123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(Optional.empty());

            // Act
            var response = userUsecase.createUser(input);

            // Assert
            assertThat(response)
                    .isExactlyInstanceOf((CreateUserOutput.NotFoundCompany.class))
                    .hasFieldOrPropertyWithValue("errorMessage", "Company not found");
        }
    }


    @Nested
    class ユーザー取得 {
        @Test
        void ユーザーIDがnullの場合_空を返す() {
            // Act
            var response = userUsecase.getUser(null);

            // Assert
            assertThat(response).isEmpty();
        }

        @Test
        void 存在しないユーザーの場合_空を返す() {
            // Arrange
            when(userRepository.findById("kazurego7")).thenReturn(Optional.empty());

            // Act
            var response = userUsecase.getUser("kazurego7");

            // Assert
            assertThat(response).isEmpty();
        }
    }

}
