package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.FinancialTarget;
import org.religion.umbanda.tad.model.financial.FinancialTargetType;
import org.religion.umbanda.tad.service.FinancialTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public FinancialTargetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialTarget> findAll() {
        return jdbcTemplate.query("select * from FinancialEntryTarget", new RowMapper<FinancialTarget>() {
            @Override
            public FinancialTarget mapRow(ResultSet resultSet, int i) throws SQLException {
                final FinancialTarget financialTarget = new FinancialTarget();
                financialTarget.setId(resultSet.getString("id"));
                financialTarget.setName(resultSet.getString("name"));
                financialTarget.setType(FinancialTargetType.fromValue(resultSet.getInt("type")));
                return financialTarget;
            }
        });
    }

}
