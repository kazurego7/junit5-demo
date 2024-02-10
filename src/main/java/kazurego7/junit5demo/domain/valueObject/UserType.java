package kazurego7.junit5demo.domain.valueObject;

import jakarta.persistence.Converter;
import kazurego7.junit5demo.utils.PersistableEnum;
import kazurego7.junit5demo.utils.PersistableEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザー種別を表す列挙型
 * 
 * 
 * <p>
 * ユーザー種別の文字列を直接扱わず、ユーザー種別を表す Enum値で生成・比較する 加えて、Enum 値をデータベースに保存する際には文字列に変換して保存する
 * 
 * Pros: ユーザー種別の文字列を直接扱わないため、ユーザー種別の文字列を間違えることがない Cons: Enum 値をデータベースに保存するための Converter
 * クラスを作成する必要がある
 * </p>
 * 
 */

@AllArgsConstructor
@Getter
public enum UserType implements PersistableEnum<String> {
    // Lombok の @AllArgsConstructor により、コンストラクタが自動生成される
    // Lombok の @Getter により、getter メソッドが自動生成される
    GENERAL("A"), ADMIN("B");

    private String value;

    // データベースに保存する際に UserType を String に変換して保存するための Converter クラス
    @Converter(autoApply = true)
    public static class UserTypeConverter extends PersistableEnumConverter<UserType, String> {
        public UserTypeConverter() {
            super(UserType.class);
        }
    }
}
