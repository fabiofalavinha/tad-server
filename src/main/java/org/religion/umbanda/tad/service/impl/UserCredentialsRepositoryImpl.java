package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
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

    private final RowMapper<UserCredentials> userCredentialsRowMapper = new RowMapper<UserCredentials>() {
        @Override
        public UserCredentials mapRow(ResultSet resultSet, int i) throws SQLException {
            final UserCredentials userCredentials = new UserCredentials();
            userCredentials.setId(UUID.fromString(resultSet.getString("id")));
            userCredentials.setUserName(resultSet.getString("username"));
            userCredentials.setPassword(Password.fromSecret(resultSet.getString("password")));
            userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("user_role")));
            final Person person = new Person();
            person.setId(userCredentials.getId());
            person.setName(resultSet.getString("name"));
            person.setGenderType(GenderType.valueOf(resultSet.getString("gender")));
            person.setBirthDate(new DateTime(resultSet.getLong("birth_date")));
            userCredentials.setPerson(person);
            return userCredentials;
        }
    };

    @Autowired
    public UserCredentialsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public UserCredentials findByUserName(String userName) {
        return jdbcTemplate.queryForObject(
            "select u.id, u.username, u.password, u.user_role, p.name, p.gender, p.birth_date from UserCredentials u inner join Person p on p.id = u.id where u.UserName = ?",
            new Object[] { userName }, userCredentialsRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public UserCredentials findById(UUID id) {
        return jdbcTemplate.queryForObject(
            "select u.id, u.username, u.password, u.user_role, p.name, p.gender, p.birth_date from UserCredentials u inner join Person p on p.id = u.id where u.Id = ?",
            new Object[] { id.toString() }, userCredentialsRowMapper);
    }

    @Transactional
    @Override
    public void update(UserCredentials userCredentials) {
        jdbcTemplate.update(
            "update UserCredentials set UserName=?, Password=?, User_Role=? where Id=?",
            userCredentials.getUserName(),
            userCredentials.getPassword().getSecret(),
            userCredentials.getUserRole().name(),
            userCredentials.getId().toString());
    }

}
