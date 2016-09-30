package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.NewsletterUser;
import org.religion.umbanda.tad.service.NewsletterUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class NewsletterUserRepositoryImpl implements NewsletterUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<NewsletterUser> newsletterUserRowMapper = (resultSet, i) -> {
        final NewsletterUser newsletterUser = new NewsletterUser();
        newsletterUser.setId(UUID.fromString(resultSet.getString("id")));
        newsletterUser.setName(resultSet.getString("name"));
        newsletterUser.setEmail(resultSet.getString("email"));
        return newsletterUser;
    };

    @Autowired
    public NewsletterUserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void save(NewsletterUser newsletterUser) {
        final UUID id = newsletterUser.getId();
        if (id == null) {
            jdbcTemplate.update("insert into NewsletterUser (id, name, email) values (?, ?, ?)",
                newsletterUser.getId().toString(),
                newsletterUser.getName(),
                newsletterUser.getEmail());
        } else {
            jdbcTemplate.update("update NewsletterUser set name=?, email=? where id=?",
                newsletterUser.getName(),
                newsletterUser.getEmail(),
                newsletterUser.getId().toString());
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
            return jdbcTemplate.queryForObject("select * from NewsletterUser where id = ?", new Object[] { id} , newsletterUserRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}