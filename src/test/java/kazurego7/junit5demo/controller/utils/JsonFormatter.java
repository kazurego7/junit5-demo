package kazurego7.junit5demo.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON文字列を整形するためのユーティリティクラス.
 */
public class JsonFormatter {

    /**
     * JSON文字列の整形に失敗した場合にスローされる例外.
     */
    public static class FormatFailedException extends RuntimeException {
        public FormatFailedException(JsonProcessingException e, String jsonString) {
            super("""
                    JSON文字列の整形に失敗しました。
                    [対象のJSON文字列]
                    %s
                    [原因]
                    %s
                    """.formatted(jsonString, e.getOriginalMessage()));
        }
    }

    /**
     * json文字列を整形して返す.
     * 
     * @param jsonString
     * @return
     */
    public static String prettify(String jsonString) {
        // json文字列を整形して返す
        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(jsonString);
            return mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new JsonFormatter.FormatFailedException(e, jsonString);
        }
    }

    /**
     * json文字列を圧縮して返す.
     * 
     * @param jsonString
     * @return
     */
    public static String minify(String jsonString) {
        // json文字列を圧縮して返す
        var mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(jsonString);
            return mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new JsonFormatter.FormatFailedException(e, jsonString);
        }
    }

    /**
     * 期待値と実測値を整形して返す.
     * 
     * @param expected
     * @param actual
     * @return
     */
    public static String prettifyDescription(String expected, String actual) {
        return """
                [JSON整形済みの比較]
                expected:
                %s
                but was:
                %s
                """.formatted(prettify(expected), prettify(actual));
    }
}
