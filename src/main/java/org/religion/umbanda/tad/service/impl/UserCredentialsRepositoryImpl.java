package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.Password;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.model.UserRole;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class UserCredentialsRepositoryImpl implements UserCredentialsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserCredentialsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public UserCredentials findByUserName(String userName) {
        final List<UserCredentials> result =
                jdbcTemplate.query(
                    "select * from UserCredentials where UserName = ?",
                    new Object[]{userName},
                    new RowMapper<UserCredentials>() {
                        @Override
                        public UserCredentials mapRow(ResultSet resultSet, int i) throws SQLException {
                            final UserCredentials userCredentials = new UserCredentials();
                            userCredentials.setId(UUID.fromString(resultSet.getString("id")));
                            userCredentials.setUserName(resultSet.getString("username"));
                            userCredentials.setPassword(Password.fromSecret(resultSet.getString("password")));
                            userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("user_role")));
                            return userCredentials;
                        }
                });
        return result.isEmpty() ? null : result.get(0);
    }

}
