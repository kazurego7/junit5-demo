package kazurego7.junit5demo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kazurego7.junit5demo.domain.valueObject.UserType;
import lombok.Data;

@Entity
@Data
@Table(schema = "demo", name = "demo_user")
public class UserEntity {
  @Id
  private String userId;
  private String userName;
  private UserType userType;
  private String mailAddress;
}
