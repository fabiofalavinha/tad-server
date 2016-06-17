package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.FinancialTarget;
import org.religion.umbanda.tad.model.financial.FinancialTargetType;
import org.religion.umbanda.tad.service.FinancialTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FinancialTargetRepositoryImpl implements FinancialTargetRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<FinancialTarget> financialTargetRowMapper = new RowMapper<FinancialTarget>() {
        @Override
        public FinancialTarget mapRow(ResultSet resultSet, int i) throws SQLException {
            final FinancialTarget financialTarget = new FinancialTarget();
            financialTarget.setId(resultSet.getString("id"));
            financialTarget.setName(resultSet.getString("name"));
            financialTarget.setType(FinancialTargetType.fromValue(resultSet.getInt("type")));
            return financialTarget;
        }
    };

    @Autowired
    public FinancialTargetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialTarget> findAll() {
        return jdbcTemplate.query("select * from FinancialEntryTarget", financialTargetRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialTarget findById(String id) {
        try {
            return jdbcTemplate.queryForObject("select * from FinancialEntryTarget where id = ?", new Object[]{id}, financialTargetRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional
    @Override
    public void create(FinancialTarget target) {
        jdbcTemplate.update("insert into FinancialEntryTarget (id, name, type) values (?, ?, ?)", target.getId(), target.getName(), target.getType().value());
    }

}
