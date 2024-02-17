package kazurego7.junit5demo.controller.utils;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * REST APIを文字列ベースでテストするためのHTTPクライアント.
 * <p>
 * 文字列でリクエストを送信し、文字列でレスポンスを受け取る。<br>
 * 共通する情報(認証・コンテンツタイプなど)はまとめて設定する。<br>
 */
public class RestTextClient {
    private TestRestTemplate restTemplate;

    public RestTextClient() {
        restTemplate = new TestRestTemplate();
    }

    /**
     * 
     * 
     * @param url
     * @param body
     * @return
     */
    public ResponseEntity<String> post(String url, String body) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var request = new HttpEntity<String>(body, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }

    public ResponseEntity<String> get(String url) {
        return restTemplate.getForEntity(url, String.class);
    }
}
