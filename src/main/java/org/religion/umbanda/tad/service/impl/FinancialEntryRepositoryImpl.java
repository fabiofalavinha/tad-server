package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.FinancialEntryRepository;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FinancialEntryRepositoryImpl implements FinancialEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<FinancialEntry> financialEntryRowMapper = new RowMapper<FinancialEntry>() {
        @Override
        public FinancialEntry mapRow(ResultSet resultSet, int i) throws SQLException {
            final FinancialEntry financialEntry = new FinancialEntry();
            financialEntry.setId(resultSet.getString("id"));
            financialEntry.setEntryDate(DateTimeUtils.fromString(resultSet.getString("entry_date"), "yyyy-MM-dd"));
            financialEntry.setValue(BigDecimal.valueOf(resultSet.getDouble("entry_value")));
            financialEntry.setBalance(new Balance(resultSet.getDouble("balance")));
            financialEntry.setAdditionalText(resultSet.getString("additional_text"));
            final FinancialReference financialReference = new FinancialReference();
            financialReference.setId(resultSet.getString("typeId"));
            financialReference.setDescription(resultSet.getString("typeDescription"));
            financialReference.setCategory(Category.fromValue(resultSet.getInt("category")));
            financialReference.setAssociatedWithCollaborator(resultSet.getBoolean("associated_with_collaborator"));
            financialEntry.setType(financialReference);
            final FinancialTarget financialTarget = new FinancialTarget();
            financialTarget.setId(resultSet.getString("targetId"));
            financialTarget.setName(resultSet.getString("targetName"));
            financialTarget.setType(FinancialTargetType.fromValue(resultSet.getInt("targetType")));
            financialEntry.setTarget(financialTarget);
            return financialEntry;
        }
    };

    @Autowired
    public FinancialEntryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialEntry> findBy(DateTime from, DateTime to) {
        final String sql =
                "select " +
                        "   e.id as id," +
                        "   e.entry_date," +
                        "   e.entry_value," +
                        "   e.balance," +
                        "   e.additional_text," +
                        "   r.id as typeId," +
                        "   r.description as typeDescription," +
                        "   r.category," +
                        "   r.associated_with_collaborator," +
                        "   t.id as targetId," +
                        "   t.name as targetName," +
                        "   t.type as targetType " +
                        "from " +
                        "   FinancialEntry e " +
                        "   inner join FinancialReference r on r.id = e.reference_entry " +
                        "   inner join FinancialEntryTarget t on t.id = e.target_id  " +
                        "where " +
                        "   strftime('%s', e.entry_date) between strftime('%s', '" + DateTimeUtils.toString(from, "yyyy-MM-dd") + "') and strftime('%s', '" + DateTimeUtils.toString(to, "yyyy-MM-dd") + "') " +
                        "order by " +
                        "   e.entry_date asc";
        return jdbcTemplate.query(sql, financialEntryRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialEntry findById(String id) {
        try {
            final String sql =
                "select " +
                        "   e.id as id," +
                        "   e.entry_date," +
                        "   e.entry_value," +
                        "   e.balance," +
                        "   e.additional_text," +
                        "   r.id as typeId," +
                        "   r.description as typeDescription," +
                        "   r.category," +
                        "   r.associated_with_collaborator," +
                        "   t.id as targetId," +
                        "   t.name as targetName," +
                        "   t.type as targetType " +
                        "from " +
                        "   FinancialEntry e " +
                        "   inner join FinancialReference r on r.id = e.reference_entry " +
                        "   inner join FinancialEntryTarget t on t.id = e.target_id  " +
                        "where " +
                        "   e.id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, financialEntryRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional
    @Override
    public void create(FinancialEntry entry) {
        jdbcTemplate.update(
                "insert into FinancialEntry (id, entry_date, entry_value, balance, reference_entry, additional_text, target_id) values (?, ?, ?, ?, ?, ?, ?)",
                entry.getId(), DateTimeUtils.toString(entry.getEntryDate(), "yyyy-MM-dd"), entry.getValue().doubleValue(), entry.getBalance().getValue().doubleValue(), entry.getType().getId(),
                entry.getAdditionalText(), entry.getTarget().getId());
    }

    @Transactional
    @Override
    public void update(FinancialEntry entry) {
        jdbcTemplate.update(
                "update FinancialEntry set entry_date=?, entry_value=?, balance=?, reference_entry=?, additional_text=?, target_id=? where id=?",
                DateTimeUtils.toString(entry.getEntryDate(), "yyyy-MM-dd"), entry.getValue().doubleValue(), entry.getBalance().getValue().doubleValue(), entry.getType().getId(),
                entry.getAdditionalText(), entry.getTarget().getId(), entry.getId());
    }
}
