package kazurego7.junit5demo.utils;

import jakarta.persistence.AttributeConverter;

public abstract class PersistableEnumConverter<E extends PersistableEnum<T>, T>
        implements AttributeConverter<E, T> {

    private Class<E> enumType;

    public PersistableEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute.getValue();
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        for (E enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.getValue().equals(dbData)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}
