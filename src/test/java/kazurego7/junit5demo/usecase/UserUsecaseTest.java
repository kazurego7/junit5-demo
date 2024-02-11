package kazurego7.junit5demo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.domain.valueObject.UserType;

@ExtendWith(MockitoExtension.class)
public class UserUsecaseTest {

    // モック
    @Mock
    private UserRepository userRepository;

    // テスト対象
    @InjectMocks
    private UserUsecase userUsecase;


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

        @Test
        void 存在するユーザーの場合_ユーザー情報を返す() {
            // Arrange
            var user = new UserEntity();
            user.setUserId("kazurego7");
            user.setUserName("numnum");
            user.setMailAddress("kazurego7@gmail.com");
            user.setUserType(UserType.GENERAL);
            when(userRepository.findById("kazurego7")).thenReturn(Optional.of(user));

            // Act
            var response = userUsecase.getUser("kazurego7");

            // Assert
            assertThat(response).isNotEmpty();
        }
    }

}
