package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.Balance;
import org.religion.umbanda.tad.service.FinancialBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class FinancialBalanceRepositoryImpl implements FinancialBalanceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FinancialBalanceRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public Balance getBalance() {
        try {
            return jdbcTemplate.queryForObject("select * from FinancialBalance", new RowMapper<Balance>() {
                @Override
                public Balance mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Balance(resultSet.getDouble("balance"));
                }
            });
        } catch (EmptyResultDataAccessException ex) {
            return new Balance();
        }
    }
}
