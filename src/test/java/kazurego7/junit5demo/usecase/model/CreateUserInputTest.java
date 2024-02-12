package kazurego7.junit5demo.usecase.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CreateUserInputTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private CreateUserInput requestBody;

    @BeforeEach
    public void setUp() {
        // テストに影響しない共通するデータを作成
        requestBody = new CreateUserInput("kazurego7", "numnum", "kazurego7@gmail.com",
                "123e4567-e89b-12d3-a456-426614174000");
    }


    @Nested
    class ユーザーID {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        public void ユーザーIDが空の場合は_不正である(String userId) {
            // Arrange
            requestBody.setUserId(userId);

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userId is required");
        }

        @Test
        public void ユーザーIDが50文字以下は_正常である() {
            // Arrange
            requestBody.setUserId("a".repeat(50));

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }

        @Test
        public void ユーザーIDが51文字以上は_不正である() {
            // Arrange
            requestBody.setUserId("a".repeat(51));

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userId is too long");
        }
    }

    @Nested
    class ユーザー名 {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        public void ユーザー名が空の場合は_不正である(String userName) {
            // Arrange
            requestBody.setUserName(userName);

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userName is required");
        }


        @Test
        public void ユーザー名が50文字以下は_正常である() {
            // Arrange
            requestBody.setUserName("a".repeat(50));

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }


        @Test
        public void ユーザー名が51文字以上は_不正である() {
            // Arrange
            requestBody.setUserName("a".repeat(51));

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userName is too long");
        }
    }

    @Nested
    class メールアドレス {
        @ParameterizedTest
        @NullAndEmptySource
        public void メールアドレスが空の場合は_不正である(String mailAddress) {
            // Arrange
            requestBody.setMailAddress(mailAddress);

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("mailAddress is required");
        }

        @Test
        public void メールアドレスが形式に沿っていない場合は_不正である() {
            // Arrange
            requestBody.setMailAddress("kazurego7@");

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("mailAddress is invalid");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "kazurego7@gmail.com",
                "kazurego7@au.com",
                "kazurego7@docomo.ne.jp",
                "kazurego7@softbank.ne.jp",
        })
        public void メールアドレスがgmailの場合は_正常である(String mailAddress) {
            // Arrange
            requestBody.setMailAddress(mailAddress);

            // Act
            var violations = validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }
    }
}
