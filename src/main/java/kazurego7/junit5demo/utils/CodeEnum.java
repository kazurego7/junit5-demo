package kazurego7.junit5demo.utils;

import java.util.Arrays;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public interface CodeEnum<E extends Enum<E>> extends AttributeConverter<E, String> {

    /** コード値を返却する */
    String getCode();

    /** [デフォルト実装] コード値が同一かどうかをチェックする */
    default boolean equalsByCode(String code) {
        return getCode().equals(code);
    }

    public default String convertToDatabaseColumn(E attribute) {
        return getCode();
    }

    @SuppressWarnings("unchecked")
    public default E convertToEntityAttribute(String dbData) {
        return Arrays.stream(this.getClass().getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .map(x -> (E) x)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + dbData));
    }
}
