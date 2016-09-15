package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.FinancialReceipt;
import org.religion.umbanda.tad.model.financial.FinancialReceiptKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
class FinancialReceiptRepositoryImpl implements FinancialReceiptRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    FinancialReceiptRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialReceiptKey generateKey() {
        try {
            return jdbcTemplate.queryForObject("select MAX(keyNumber) as keyNumber from FinancialReceiptId", new RowMapper<FinancialReceiptKey>() {
                @Override
                public FinancialReceiptKey mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new FinancialReceiptKey(resultSet.getInt("keyNumber") + 1, DateTime.now().getYear());
                }
            });
        } catch (Exception ex) {
            return FinancialReceiptKey.geneterateDefault();
        }
    }

    @Override
    @Transactional
    public void create(FinancialReceipt financialReceipt) {
        int rowAffected = jdbcTemplate.update("update FinancialReceiptId set keyNumber=?", financialReceipt.getKey().getNumber());
        if (rowAffected == 0) {
            jdbcTemplate.update("insert into FinancialReceiptId (keyNumber) values (?)", financialReceipt.getKey().getNumber());
        }
        jdbcTemplate.update("insert into FinancialReceipt (keyNumber, keyYear, created, status, entry_id) values (?, ?, ?, ?, ?)", financialReceipt.getKey().getNumber(), financialReceipt.getKey().getYear(), financialReceipt.getCreated().getMillis(), financialReceipt.getStatus().value(), financialReceipt.getFinancialEntry().getId());
    }

    @Override
    @Transactional
    public void update(FinancialReceipt financialReceipt) {
        jdbcTemplate.update("update FinancialReceipt set sent=?, status=? where keyNumber=? and keyYear=?", financialReceipt.getSent() == null ? null : financialReceipt.getSent().getMillis(), financialReceipt.getStatus().value(), financialReceipt.getKey().getNumber(), financialReceipt.getKey().getYear());
    }
}
