package kazurego7.junit5demo.controller.model;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;


public class CreateUserRequestBodyTest {

        private final Validator validator =
                        Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void userId_is_required() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody(null, "numnum", "kazurego7@gmail.com");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("userId is required");
        }

        @Test
        public void userId_is_blank() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("", "numnum", "kazurego7@gmail.com");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("userId is required");
        }

        @Test
        public void userName_is_required() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("userId", null, "kazurego7@gmail.com");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("userName is required");
        }

        @Test
        public void userName_is_blank() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("userId", " ", "kazurego7@gmail.com");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("userName is required");
        }

        @Test
        public void mailAddress_is_required() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("userId", "numnum", null);

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("mailAddress is required");
        }

        @Test
        public void mailAddress_is_blank() {
                // Arrange
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("userId", "numnum", "");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("mailAddress is required");
        }

        @Test
        public void userId_is_too_long() {
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

        @Test
        public void userName_is_too_long() {
                // Arrange
                String userName = "a".repeat(51);
                CreateUserRequestBody requestBody =
                                new CreateUserRequestBody("userId", userName,
                                                "kazurego7@gmail.com");

                // Act
                Set<ConstraintViolation<CreateUserRequestBody>> violations =
                                validator.validate(requestBody);

                // Assert
                assertThat(violations).hasSize(1);
                assertThat(violations).extracting("message").contains("userName is too long");
        }



        @Test
        public void mailAddress_is_invalid() {
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
}
