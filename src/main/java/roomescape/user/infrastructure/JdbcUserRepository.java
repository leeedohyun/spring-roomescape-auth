package roomescape.user.infrastructure;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User findUser = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ), email);
            return Optional.ofNullable(findUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
