package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.NewsletterUser;
import org.religion.umbanda.tad.model.NewsletterUserConfirmationStatus;
import org.religion.umbanda.tad.service.NewsletterUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NewsletterUserRepositoryImpl implements NewsletterUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<NewsletterUser> newsletterUserRowMapper = (resultSet, i) -> {
        final NewsletterUser newsletterUser = new NewsletterUser();
        newsletterUser.setId(UUID.fromString(resultSet.getString("id")));
        newsletterUser.setName(resultSet.getString("name"));
        newsletterUser.setEmail(resultSet.getString("email"));
        final Optional<NewsletterUserConfirmationStatus> statusMaybe = NewsletterUserConfirmationStatus.fromValue(resultSet.getInt("status"));
        if (statusMaybe.isPresent()) {
            newsletterUser.setStatus(statusMaybe.get());
        }
        return newsletterUser;
    };

    @Autowired
    public NewsletterUserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void save(NewsletterUser newsletterUser) {
        final String id = newsletterUser.getId().toString();
        final boolean existed = jdbcTemplate.queryForObject("select count(*) from NewsletterUser where id = ?", Integer.class, id) == 1;
        if (existed) {
            jdbcTemplate.update("update NewsletterUser set name=?, email=?, status=? where id=?", newsletterUser.getName(), newsletterUser.getEmail(), newsletterUser.getStatus().value(), id);
        } else {
            jdbcTemplate.update("insert into NewsletterUser (id, name, email, status) values (?, ?, ?, ?)", id, newsletterUser.getName(), newsletterUser.getEmail(), newsletterUser.getStatus().value());
        }
    }

    @Transactional
    @Override
    public void removeById(String id) {
        jdbcTemplate.update("delete from NewsletterUser where id=?", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsletterUser> findAll() {
        return jdbcTemplate.query("select * from NewsletterUser order by name", newsletterUserRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return jdbcTemplate.queryForObject("select count(*) from NewsletterUser where email = ?", Integer.class, email) == 1;
    }

    @Transactional(readOnly = true)
    @Override
    public NewsletterUser findById(String id) {
        try {
            return jdbcTemplate.queryForObject("select * from NewsletterUser where id = ?", new Object[] { id } , newsletterUserRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}