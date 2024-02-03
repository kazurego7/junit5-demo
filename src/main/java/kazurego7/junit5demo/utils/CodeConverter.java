package kazurego7.junit5demo.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public abstract class CodeConverter<E extends Enum<E>>
        implements AttributeConverter<CodeEnum<E>, String> {

    public abstract Class<? extends CodeEnum<E>> getClazz();

    @Override
    public String convertToDatabaseColumn(CodeEnum<E> attribute) {
        return attribute.getCode();
    }

    @Override
    public CodeEnum<E> convertToEntityAttribute(String dbData) {
        return CodeEnum.of(this.getClazz(), dbData);
    }
}
