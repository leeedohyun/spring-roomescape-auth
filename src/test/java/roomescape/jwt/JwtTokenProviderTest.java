package roomescape.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.user.domain.Role;
import roomescape.user.domain.User;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        jwtTokenProvider = new JwtTokenProvider(secretKey, 1000L);
    }

    @Test
    @DisplayName("JWT 토큰을 생성한다.")
    void createJwt() {
        // given
        User user = new User(1L, "name", "email@email.com", "password", Role.USER);

        // when
        String jwt = jwtTokenProvider.createJwt(user);

        // then
        assertThat(jwt).isNotNull();
    }

    @Test
    void 토큰에서_이메일을_추출한다() {
        // given
        User user = new User(1L, "name", "email@email.com", "password", Role.USER);
        String jwt = jwtTokenProvider.createJwt(user);

        // when
        String extractedEmail = jwtTokenProvider.getEmail(jwt);

        // then
        assertThat(extractedEmail).isEqualTo(user.getEmail());
    }

    @Test
    void 토큰이_빈문자열이면_NULL을_반환한다() {
        // given
        String jwt = "";

        // when
        String extractedEmail = jwtTokenProvider.getEmail(jwt);

        // then
        assertThat(extractedEmail).isNull();
    }

    @Test
    void 토큰에서_유저의_ROLE을_추출한다() {
        // given
        User user = new User(1L, "name", "email@email.com", "password", Role.USER);
        String jwt = jwtTokenProvider.createJwt(user);

        // when
        Role extractedRole = jwtTokenProvider.getRole(jwt);

        // then
        assertThat(extractedRole).isEqualTo(Role.USER);
    }
}
