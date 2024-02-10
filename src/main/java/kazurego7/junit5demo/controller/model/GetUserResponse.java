
package kazurego7.junit5demo.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
    private String userId;
    private String userName;
    private String mailAddress;
    private String userType;
}
