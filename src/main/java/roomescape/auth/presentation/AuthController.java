package roomescape.auth.presentation;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.application.AuthService;
import roomescape.auth.dto.CheckUserInfoResponse;
import roomescape.auth.dto.LoginRequest;

@RestController
public class AuthController {

    private static final String TOKEN_COOKIE_NAME = "token";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);
        Cookie cookie = createCookie(accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckUserInfoResponse> checkUserInfo(HttpServletRequest request) {
        String accessToken = extractTokenFromCookie(request.getCookies());
        CheckUserInfoResponse response = authService.checkUserInfo(accessToken);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = createCookie("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
