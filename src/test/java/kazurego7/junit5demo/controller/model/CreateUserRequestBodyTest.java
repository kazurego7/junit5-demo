package kazurego7.junit5demo.controller.model;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class CreateUserRequestBodyTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    class ユーザーID {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        public void ユーザーIDが空の場合は_不正である(String userId) {
            // Arrange
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody(userId, "numnum", "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userId is required");
        }

        @Test
        public void ユーザーIDが50文字以下は_正常である() {
            // Arrange
            String userId = "a".repeat(50);
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody(userId, "numnum", "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }

        @Test
        public void ユーザーIDが51文字以上は_不正である() {
            // Arrange
            String userId = "a".repeat(51);
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody(userId, "numnum", "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

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
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", userName,
                            "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("userName is required");
        }


        @Test
        public void ユーザー名が50文字以下は_正常である() {
            // Arrange
            String userName = "a".repeat(50);
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", userName, "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }


        @Test
        public void ユーザー名が51文字以上は_不正である() {
            // Arrange
            String userName = "a".repeat(51);
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", userName, "kazurego7@gmail.com");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

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
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", "numnum", mailAddress);

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("mailAddress is required");
        }

        @Test
        public void メールアドレスが形式に沿っていない場合は_不正である() {
            // Arrange
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", "numnum", "invalid email");

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

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
            CreateUserRequestBody requestBody =
                    new CreateUserRequestBody("userId", "numnum", mailAddress);

            // Act
            Set<ConstraintViolation<CreateUserRequestBody>> violations =
                    validator.validate(requestBody);

            // Assert
            assertThat(violations).hasSize(0);
        }
    }
}
