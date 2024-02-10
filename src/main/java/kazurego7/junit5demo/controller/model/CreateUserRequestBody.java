package kazurego7.junit5demo.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestBody(
                @NotBlank(message = "userId is required")
                @Max(value = 50, message = "userId is too long")
                String userId,

                @NotBlank(message = "userName is required")
                @Max(value = 50, message = "userName is too long")
                String userName,

                @NotBlank(message = "mailAddress is required")
                @Email(message = "mailAddress is invalid",
                                regexp = "^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\\\.)+[a-zA-Z]{2,}$")
                String mailAddress) {
}
