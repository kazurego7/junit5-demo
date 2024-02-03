package kazurego7.junit5demo.utils;

import java.util.Arrays;

public interface CodeEnum<E extends Enum<E>> {

    /** コード値を返却する */
    String getCode();

    static <E extends Enum<E>> CodeEnum<E> of(Class<? extends CodeEnum<E>> clazz, String code) {
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such code: " + code));
    }
}
