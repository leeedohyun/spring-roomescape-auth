package roomescape.auth.application;

import org.springframework.stereotype.Service;

import roomescape.auth.dto.CheckUserInfoResponse;
import roomescape.jwt.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.auth.dto.LoginRequest;
import roomescape.user.exception.PasswordNotMatchException;
import roomescape.user.exception.UserNotFoundException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.email());
        if (user.isNotMatchPassword(loginRequest.password())) {
            throw new PasswordNotMatchException();
        }

        return jwtTokenProvider.createJwt(user.getEmail());
    }

    public CheckUserInfoResponse checkUserInfo(String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        User user = getUserByEmail(email);
        return new CheckUserInfoResponse(user.getName());
    }

    public void logout(String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        getUserByEmail(email);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
