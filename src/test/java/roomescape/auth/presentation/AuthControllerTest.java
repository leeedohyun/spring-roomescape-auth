package roomescape.auth.presentation;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Test
    @DisplayName("로그인에 성공한다.")
    void login() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .header("Set-Cookie", Matchers.matchesPattern("token=.*; Path=/; HttpOnly"));
    }

    @Test
    @DisplayName("회원 가입이 되지 않은 이매일인 경우 로그인에 실패한다.")
    void loginFail_WhenInvalidEmail() {
        LoginRequest loginRequest = new LoginRequest("admi@email.com", "password");

        RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("비밀번호가 틀린 경우 로그인에 실패한다.")
    void loginFail_WhenPasswordNotMatch() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "1234");

        RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
