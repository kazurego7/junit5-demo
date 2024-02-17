
package kazurego7.junit5demo.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserOutput {
    private String userId;
    private String userName;
    private String mailAddress;
    private String userType;
    private String companyId;
    private String companyName;
    private String companyAddress;
}
