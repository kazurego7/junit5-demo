package kazurego7.junit5demo.usecase.model;

public sealed interface CreateUserOutput {
    public record Success() implements CreateUserOutput {
    }
    public record DuplicatedUser(String errorMessage) implements CreateUserOutput {
    }
    public record NotFoundCompany(String errorMessage) implements CreateUserOutput {
    }
}
