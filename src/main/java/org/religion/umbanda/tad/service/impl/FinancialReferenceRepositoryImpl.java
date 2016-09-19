package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.Category;
import org.religion.umbanda.tad.model.financial.FinancialReference;
import org.religion.umbanda.tad.service.FinancialReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FinancialReferenceRepositoryImpl implements FinancialReferenceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<FinancialReference> financialReferenceRowMapper = new RowMapper<FinancialReference>() {
        @Override
        public FinancialReference mapRow(ResultSet resultSet, int i) throws SQLException {
            final FinancialReference financialReference = new FinancialReference();
            financialReference.setId(resultSet.getString("id"));
            financialReference.setDescription(resultSet.getString("description"));
            financialReference.setCategory(Category.fromValue(resultSet.getInt("category")));
            financialReference.setAssociatedWithCollaborator(resultSet.getBoolean("associated_with_collaborator"));
            return financialReference;
        }
    };

    @Autowired
    public FinancialReferenceRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialReference> findAll() {
        return jdbcTemplate.query("select * from FinancialReference", financialReferenceRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialReference findById(String id) {
        try {
            return jdbcTemplate.queryForObject("select * from FinancialReference where id = ?", new Object[]{id}, financialReferenceRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<FinancialReference> findByDescription(String description) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("select * from FinancialReference where description like ?", financialReferenceRowMapper, "%" + description + "%"));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void create(FinancialReference financialReference) {
        jdbcTemplate.update("insert into FinancialReference (id, description, category, associated_with_collaborator) values (?, ?, ?, ?)",
                financialReference.getId(),
                financialReference.getDescription(),
                financialReference.getCategory().getValue(),
                financialReference.isAssociatedWithCollaborator());
    }

    @Transactional
    @Override
    public void update(FinancialReference financialReference) {
        jdbcTemplate.update("update FinancialReference set description=?, category=?, associated_with_collaborator=? where id=?",
                financialReference.getDescription(),
                financialReference.getCategory().getValue(),
                financialReference.isAssociatedWithCollaborator(),
                financialReference.getId());
    }

    @Transactional
    @Override
    public void removeById(String id) {
        jdbcTemplate.update("delete from FinancialReference where id = ?", id);
    }

}
