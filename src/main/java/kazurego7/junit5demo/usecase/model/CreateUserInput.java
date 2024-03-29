package kazurego7.junit5demo.usecase.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInput {
    @NotBlank(message = "userId is required")
    @Size(max = 50, message = "userId is too long")
    private String userId;

    @NotBlank(message = "userName is required")
    @Size(max = 50, message = "userName is too long")
    private String userName;

    @NotBlank(message = "mailAddress is required")
    @Email(message = "mailAddress is invalid")
    private String mailAddress;

    @NotBlank(message = "companyId is required")
    @Size(max = 50, message = "companyId is too long")
    private String companyId;
}
