package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.CollaboratorRepository;
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
public class CollaboratorRepositoryImpl implements CollaboratorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CollaboratorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Collaborator> findAll() {
        final String query =
            "select " +
            "    p.id, " +
            "    p.name, " +
            "    p.gender, " +
            "    p.birth_date, " +
            "    c.start_date, " +
            "    c.release_date, " +
            "    c.observation, " +
            "    u.id as user_credentials_id, " +
            "    u.username, " +
            "    u.password, " +
            "    u.user_role " +
            "from " +
            "    Collaborator c " +
            "    inner join Person p on p.id = c.person_id " +
            "    inner join UserCredentials u on u.id = c.usercredentials_id " +
            "order by " +
            "    p.name";

        final List<Collaborator> collaborators =
            jdbcTemplate.query(
                query,
                new Object[] {},
                new RowMapper<Collaborator>() {
                    @Override
                    public Collaborator mapRow(ResultSet resultSet, int i) throws SQLException {
                        final Person person = new Person();
                        person.setId(UUID.fromString(resultSet.getString("id")));
                        person.setName(resultSet.getString("name"));
                        person.setGenderType(GenderType.valueOf(resultSet.getString("gender")));
                        person.setBirthDate(new DateTime(resultSet.getLong("birth_date")));

                        final UserCredentials userCredentials = new UserCredentials();
                        userCredentials.setId(UUID.fromString(resultSet.getString("user_credentials_id")));
                        userCredentials.setUserName(resultSet.getString("username"));
                        userCredentials.setPassword(Password.fromSecret(resultSet.getString("password")));
                        userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("user_role")));

                        final Collaborator collaborator = new Collaborator();
                        collaborator.setPerson(person);
                        collaborator.setUserCredentials(userCredentials);
                        collaborator.setStartDate(new DateTime(resultSet.getLong("start_date")));
                        final long releaseDateTimeInMillis = resultSet.getLong("release_date");
                        if (releaseDateTimeInMillis > 0) {
                            collaborator.setReleaseDate(new DateTime(releaseDateTimeInMillis));
                        }
                        collaborator.setObservation(resultSet.getString("observation"));

                        return collaborator;
                    }
                });

        for (Collaborator collaborator : collaborators) {
            final List<Telephone> telephones =
                jdbcTemplate.query(
                    "select * from Telephone where person_id = ?",
                    new Object[] { collaborator.getPerson().getId().toString() },
                    new RowMapper<Telephone>() {
                        @Override
                        public Telephone mapRow(ResultSet resultSet, int i) throws SQLException {
                            final Telephone telephone = new Telephone();
                            telephone.setId(UUID.fromString(resultSet.getString("id")));
                            telephone.setAreaCode(resultSet.getInt("area_code"));
                            telephone.setNumber(resultSet.getInt("number"));
                            telephone.setPhoneType(PhoneType.valueOf(resultSet.getString("phone_type")));
                            return telephone;
                        }
                });
            collaborator.getPerson().setTelephones(telephones);
        }

        return collaborators;
    }

    @Transactional(readOnly = true)
    @Override
    public Collaborator findById(UUID id) {
        final String query =
            "select " +
            "    p.id, " +
            "    p.name, " +
            "    p.gender, " +
            "    p.birth_date, " +
            "    c.start_date, " +
            "    c.release_date, " +
            "    c.observation, " +
            "    u.id as user_credentials_id, " +
            "    u.username, " +
            "    u.password, " +
            "    u.user_role " +
            "from " +
            "    Collaborator c " +
            "    inner join Person p on p.id = c.person_id " +
            "    inner join UserCredentials u on u.id = c.usercredentials_id " +
            "where  " +
            "    c.person_id = ? " +
            "order by " +
            "    p.name";

        final Collaborator collaborator =
            jdbcTemplate.queryForObject(
                query,
                new Object[] { id.toString() },
                new RowMapper<Collaborator>() {
                    @Override
                    public Collaborator mapRow(ResultSet resultSet, int i) throws SQLException {
                        final Person person = new Person();
                        person.setId(UUID.fromString(resultSet.getString("id")));
                        person.setName(resultSet.getString("name"));
                        person.setGenderType(GenderType.valueOf(resultSet.getString("gender")));
                        person.setBirthDate(new DateTime(resultSet.getLong("birth_date")));

                        final UserCredentials userCredentials = new UserCredentials();
                        userCredentials.setId(UUID.fromString(resultSet.getString("user_credentials_id")));
                        userCredentials.setUserName(resultSet.getString("username"));
                        userCredentials.setPassword(Password.fromSecret(resultSet.getString("password")));
                        userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("user_role")));

                        final Collaborator collaborator = new Collaborator();
                        collaborator.setPerson(person);
                        collaborator.setUserCredentials(userCredentials);
                        collaborator.setStartDate(new DateTime(resultSet.getLong("start_date")));
                        collaborator.setReleaseDate(new DateTime(resultSet.getLong("release_date")));
                        collaborator.setObservation(resultSet.getString("observation"));

                        return collaborator;
                    }
                });

        final List<Telephone> telephones =
            jdbcTemplate.query(
                "select * from Telephone where person_id = ?",
                new Object[] { collaborator.getPerson().getId().toString() },
                new RowMapper<Telephone>() {
                    @Override
                    public Telephone mapRow(ResultSet resultSet, int i) throws SQLException {
                        final Telephone telephone = new Telephone();
                        telephone.setId(UUID.fromString(resultSet.getString("id")));
                        telephone.setAreaCode(resultSet.getInt("area_code"));
                        telephone.setNumber(resultSet.getInt("number"));
                        telephone.setPhoneType(PhoneType.valueOf(resultSet.getString("phone_type")));
                        return telephone;
                    }
                });

        collaborator.getPerson().setTelephones(telephones);

        return collaborator;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(UUID id) {
        return jdbcTemplate.queryForObject("select count(*) from Collaborator where person_id=?", Integer.class, id.toString()) == 1;
    }

    @Transactional
    @Override
    public void removeById(UUID id) {
        if (existsById(id)) {
            doRemoveById(id.toString());
        }
    }

    @Transactional
    @Override
    public void addCollaborator(Collaborator newCollaborator) {
        final Person person = newCollaborator.getPerson();
        final UserCredentials userCredentials = newCollaborator.getUserCredentials();

        jdbcTemplate.update("insert into Person (id, name, gender, birth_date) values (?, ?, ?, ?)",
            person.getId().toString(),
            person.getName(),
            person.getGenderType().name(),
            person.getBirthDate().getMillis());

        for (Telephone telephone : person.getTelephones()) {
            jdbcTemplate.update("insert into Telephone (id, area_code, number, phone_type, person_id) values (?, ?, ?, ?, ?)",
                telephone.getId().toString(),
                telephone.getAreaCode(),
                telephone.getNumber(),
                telephone.getPhoneType().name(),
                person.getId().toString());
        }

        jdbcTemplate.update("insert into Collaborator (person_id, start_date, release_date, observation, usercredentials_id) values (?, ?, ?, ?, ?)",
            person.getId().toString(),
            newCollaborator.getStartDate().getMillis(),
            newCollaborator.getReleaseDate() == null ? 0 : newCollaborator.getReleaseDate().getMillis(),
            newCollaborator.getObservation(),
            userCredentials.getId().toString());

        jdbcTemplate.update("insert into UserCredentials (id, username, password, user_role) values (?, ?, ?, ?)",
            userCredentials.getId().toString(),
            userCredentials.getUserName(),
            userCredentials.getPassword().getSecret(),
            userCredentials.getUserRole().name());
    }

    @Transactional
    @Override
    public void updateCollaborator(Collaborator newCollaborator) {
        doRemoveById(newCollaborator.getPerson().getId().toString());
        addCollaborator(newCollaborator);
    }

    private void doRemoveById(String id) {
        jdbcTemplate.update("delete from UserCredentials where id = ?", id);
        jdbcTemplate.update("delete from Collaborator where person_id = ?", id);
        jdbcTemplate.update("delete from Telephone where person_id = ?", id);
        jdbcTemplate.update("delete from Person where id = ?", id);
    }

}
