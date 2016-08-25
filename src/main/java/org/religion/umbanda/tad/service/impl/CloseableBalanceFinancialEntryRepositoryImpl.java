package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Password;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.model.UserRole;
import org.religion.umbanda.tad.model.financial.Balance;
import org.religion.umbanda.tad.model.financial.CloseableBalanceFinancialEntry;
import org.religion.umbanda.tad.service.CloseableBalanceFinancialEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class CloseableBalanceFinancialEntryRepositoryImpl implements CloseableBalanceFinancialEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CloseableBalanceFinancialEntryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void create(CloseableBalanceFinancialEntry closeableBalanceFinancialEntry) {
        jdbcTemplate.update(
                "insert into CloseableBalanceFinancialEntry (id, closed, closed_by, last_balance) values (?, ?, ?, ?)",
                closeableBalanceFinancialEntry.getId(),
                closeableBalanceFinancialEntry.getClosedDate().getMillis(),
                closeableBalanceFinancialEntry.getClosedBy().getId().toString(),
                closeableBalanceFinancialEntry.getBalance().getValue().doubleValue());
    }

    @Transactional(readOnly = true)
    @Override
    public CloseableBalanceFinancialEntry getLastCloseableBalanceFinancialEntry() {
        try {
            return jdbcTemplate.queryForObject(
                    "select" +
                            "   e.id, " +
                            "   e.closed, " +
                            "   e.last_balance, " +
                            "   u.id as closedByUserId, " +
                            "   u.username as closedByUserName, " +
                            "   u.password as closedByUserPassword, " +
                            "   u.user_role as closedByUserRole " +
                            "from " +
                            "   CloseableBalanceFinancialEntry e " +
                            "   inner join UserCredentials u on u.id = e.closed_by " +
                            "order by e.closed desc limit 1", new RowMapper<CloseableBalanceFinancialEntry>() {
                        @Override
                        public CloseableBalanceFinancialEntry mapRow(ResultSet resultSet, int i) throws SQLException {
                            final CloseableBalanceFinancialEntry closeableBalanceFinancialEntry = new CloseableBalanceFinancialEntry();
                            closeableBalanceFinancialEntry.setId(UUID.fromString(resultSet.getString("id")));
                            closeableBalanceFinancialEntry.setClosedDate(new DateTime(resultSet.getLong("closed")));
                            closeableBalanceFinancialEntry.setBalance(new Balance(resultSet.getDouble("last_balance")));
                            final UserCredentials userCredentials = new UserCredentials();
                            userCredentials.setId(UUID.fromString(resultSet.getString("closedByUserId")));
                            userCredentials.setUserName(resultSet.getString("closedByUserName"));
                            userCredentials.setPassword(Password.fromSecret(resultSet.getString("closedByUserPassword")));
                            userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("closedByUserRole")));
                            closeableBalanceFinancialEntry.setClosedBy(userCredentials);
                            return closeableBalanceFinancialEntry;
                        }
                    });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
