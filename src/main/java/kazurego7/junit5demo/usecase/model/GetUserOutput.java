
package kazurego7.junit5demo.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserOutput {
    private String userId;
    private String userName;
    private String mailAddress;
    private String userType;
}
