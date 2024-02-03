package kazurego7.junit5demo.controller;

import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequestBody(
        @NotEmpty(message = "userName is required")
        String userName,
        @NotEmpty(message = "mailAddress is required")
        String mailAddress) {
}
