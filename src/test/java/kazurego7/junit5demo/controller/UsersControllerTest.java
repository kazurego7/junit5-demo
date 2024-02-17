package kazurego7.junit5demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import kazurego7.junit5demo.controller.utils.JsonFormatter;
import kazurego7.junit5demo.controller.utils.RestTextClient;
import kazurego7.junit5demo.domain.entity.CompanyEntity;
import kazurego7.junit5demo.domain.entity.UserEntity;
import kazurego7.junit5demo.domain.repository.CompanyRepository;
import kazurego7.junit5demo.domain.repository.UserRepository;
import kazurego7.junit5demo.domain.valueObject.UserType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersControllerTest {

    @LocalServerPort
    private int port;

    RestTextClient restTextClient = new RestTextClient();

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // テスト前にデータをクリア
        userRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Nested
    class ユーザー登録 {

        @Test
        void 既存ユーザーが存在しない場合はユーザーが追加できる() {
            // Arrange
            // 会社を登録
            var company = new CompanyEntity();
            company.setCompanyId("123e4567-e89b-12d3-a456-426614174000");
            company.setCompanyName("株式会社テスト");
            company.setAddress("000-1111 テスト県テスト市テスト町1-2-3");
            companyRepository.save(company);

            // Act
            var postResponse = restTextClient.post("http://localhost:" + port + "/users", """
                    {
                        "userId": "kazurego7",
                        "userName": "numnum",
                        "mailAddress": "kazurego7@gmail.com",
                        "companyId": "%s"
                    }
                    """.formatted(company.getCompanyId()));// companyId は自動生成されたIDを使用

            // Assert
            // httpステータスが202であること
            assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

            // ユーザーが登録されていること
            assertThat(userRepository.findById("kazurego7"))
                    .isNotEmpty() // ユーザーが存在する
                    .hasValueSatisfying(user -> { // ユーザーの内容を検証
                        assertThat(user.getUserId()).isEqualTo("kazurego7");
                        assertThat(user.getUserName()).isEqualTo("numnum");
                        assertThat(user.getMailAddress()).isEqualTo("kazurego7@gmail.com");
                        assertThat(user.getUserType()).isEqualTo(UserType.GENERAL);
                        assertThat(user.getCompany().getCompanyId())
                                .isEqualTo(company.getCompanyId());
                    });
        }
    }

    @Nested
    class ユーザー詳細取得 {

        @Test
        void ユーザーが存在する場合はユーザー詳細を取得できる() {
            // Arrange
            // 会社とユーザーを登録
            var company = new CompanyEntity();
            company.setCompanyName("株式会社テスト");
            company.setAddress("000-1111 テスト県テスト市テスト町1-2-3");
            companyRepository.save(company);
            var user = new UserEntity();
            user.setUserId("kazurego7");
            user.setUserName("numnum");
            user.setMailAddress("kazurego7@gmail.com");
            user.setUserType(UserType.GENERAL);
            user.setCompany(company);
            userRepository.save(user);

            // Act
            var getResponse = restTextClient.get("http://localhost:" + port + "/users/kazurego7");

            // Assert
            // httpステータスが200であること
            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            // ユーザー詳細が取得できること
            var expected = JsonFormatter.minify("""
                    {
                        "userId" : "kazurego7",
                        "userName" : "numnum",
                        "mailAddress" : "kazurego7@gmail.com",
                        "userType" : "A",
                        "companyId" : "%s",
                        "companyName" : "株式会社テスト",
                        "companyAddress" : "000-1111 テスト県テスト市テスト町1-2-3"
                    }
                    """.formatted(company.getCompanyId()));// companyId は自動生成されたIDを使用
            var actual = getResponse.getBody();
            assertThat(actual)
                    .as(JsonFormatter.prettifyDescription(expected, actual))
                    .isEqualTo(expected);
        }
    }
}
