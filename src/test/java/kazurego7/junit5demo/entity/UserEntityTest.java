package kazurego7.junit5demo.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import kazurego7.junit5demo.domain.entity.UserEntity;

public class UserEntityTest {

  @Test
  void Email_is_invalid() {
    var user = new UserEntity();
    // 確認
    assertThrows(RuntimeException.class,
        () -> {
          user.setMailAddress("kazurego7");
        });
  }
}
