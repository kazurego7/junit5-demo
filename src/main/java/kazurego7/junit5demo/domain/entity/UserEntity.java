package kazurego7.junit5demo.domain.entity;

import java.util.regex.Pattern;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import kazurego7.junit5demo.domain.value.UserType;
import lombok.Data;

@Entity
@Data
public class UserEntity {
  @Id
  private String userId;
  private String userName;
  private UserType userType;
  private String mailAddress;

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setUserName(String userName) {
    if (!isValidUserName(userName)) {
      throw new RuntimeException();
    }
    this.userName = userName;
  }

  public void setMailAddress(String mailAddress) {
    if (!isValidMailAddress(mailAddress)) {
      throw new RuntimeException();
    }
    this.mailAddress = mailAddress;
  }

  public Boolean isValidUserName(String userName) {
    return StringUtils.isNotEmpty(userName);
  }

  public Boolean isValidMailAddress(String mailAddress) {
    var isValid = Pattern
        .compile("^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$")
        .matcher(mailAddress)
        .find();
    return isValid;
  }
}
