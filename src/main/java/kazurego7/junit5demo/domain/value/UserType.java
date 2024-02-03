package kazurego7.junit5demo.domain.value;

import kazurego7.junit5demo.utils.CodeConverter;
import kazurego7.junit5demo.utils.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType implements CodeEnum<UserType> {
    GENERAL("A"), ADMIN("B");

    private String code;

    public class UserTypeConverter extends CodeConverter<UserType> {
        @Override
        public Class<? extends CodeEnum<UserType>> getClazz() {
            return UserType.class;
        }
    }
}
