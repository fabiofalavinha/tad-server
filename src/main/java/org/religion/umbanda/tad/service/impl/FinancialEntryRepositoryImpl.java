package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Password;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.model.UserRole;
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
import java.util.List;
import java.util.UUID;

@Repository
public class FinancialEntryRepositoryImpl implements FinancialEntryRepository {

    private enum OpenedFinancialEntryDirection {

        FIRST("asc"),
        LAST("desc");

        private final String sortDirection;

        OpenedFinancialEntryDirection(String sortDirection) {
            this.sortDirection = sortDirection;
        }

        public String sortDirection() {
            return sortDirection;
        }
    }

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<FinancialEntry> financialEntryRowMapper = (resultSet, i) -> {
        final FinancialEntry financialEntry = new FinancialEntry();
        financialEntry.setId(resultSet.getString("id"));
        financialEntry.setEntryDate(DateTimeUtils.fromString(resultSet.getString("entry_date"), "yyyy-MM-dd"));
        financialEntry.setValue(BigDecimal.valueOf(resultSet.getDouble("entry_value")));
        financialEntry.setBalance(new Balance(resultSet.getDouble("balance")));
        financialEntry.setAdditionalText(resultSet.getString("additional_text"));
        financialEntry.setStatus(FinancialEntryStatus.fromValue(resultSet.getInt("status")));
        final String closedByUserId = resultSet.getString("closedByUserId");
        if (closedByUserId != null && !closedByUserId.isEmpty()) {
            final CloseableFinancialEntry closeableFinancialEntry = new CloseableFinancialEntry();
            final UserCredentials userCredentials = new UserCredentials();
            userCredentials.setId(UUID.fromString(closedByUserId));
            userCredentials.setUserName(resultSet.getString("closedByUserName"));
            userCredentials.setPassword(Password.fromSecret(resultSet.getString("closedByUserPassword")));
            userCredentials.setUserRole(UserRole.valueOf(resultSet.getString("closedByUserRole")));
            closeableFinancialEntry.setClosedBy(userCredentials);
            closeableFinancialEntry.setClosedDate(new DateTime(resultSet.getLong("closed")));
            financialEntry.setCloseableFinancialEntry(closeableFinancialEntry);
        }
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
        int receiptNumber = resultSet.getInt("receiptNumber");
        if (receiptNumber != 0) {
            FinancialReceiptInfo financialReceiptInfo = new FinancialReceiptInfo();
            financialReceiptInfo.setNumber(FinancialReceiptKey.from(receiptNumber, resultSet.getInt("receiptYear")));
            financialReceiptInfo.setSent(new DateTime(resultSet.getLong("receiptSentDateMillis")));
            financialEntry.setFinancialReceiptInfo(financialReceiptInfo);
        }
        return financialEntry;
    };

    @Autowired
    public FinancialEntryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialEntry> findBy(DateTime from, DateTime to) {
        from = from.minusDays(1);
        final String sql =
                "select " +
                "   e.id as id," +
                "   e.entry_date," +
                "   e.entry_value," +
                "   e.balance," +
                "   e.additional_text," +
                "   e.closed, " +
                "   e.status, " +
                "   u.id as closedByUserId, " +
                "   u.username as closedByUserName, " +
                "   u.password as closedByUserPassword, " +
                "   u.user_role as closedByUserRole, " +
                "   r.id as typeId," +
                "   r.description as typeDescription," +
                "   r.category," +
                "   r.associated_with_collaborator," +
                "   t.id as targetId," +
                "   t.name as targetName," +
                "   t.type as targetType, " +
                "   s.keyNumber as receiptNumber, " +
                "   s.keyYear as receiptYear, " +
                "   s.sent as receiptSentDateMillis " +
                "from " +
                "   FinancialEntry e " +
                "   inner join FinancialReference r on r.id = e.reference_entry " +
                "   inner join FinancialEntryTarget t on t.id = e.target_id " +
                "   left join FinancialReceipt s on s.entry_id = e.id " +
                "   left join UserCredentials u on u.id = e.closed_by " +
                "where " +
                "   e.entry_date >= datetime('" + DateTimeUtils.toString(from, "yyyy-MM-dd") + "') and e.entry_date <= datetime('" + DateTimeUtils.toString(to, "yyyy-MM-dd") + "') " +
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
                "   e.closed, " +
                "   e.status, " +
                "   u.id as closedByUserId, " +
                "   u.username as closedByUserName, " +
                "   u.password as closedByUserPassword, " +
                "   u.user_role as closedByUserRole, " +
                "   r.id as typeId," +
                "   r.description as typeDescription," +
                "   r.category," +
                "   r.associated_with_collaborator," +
                "   t.id as targetId," +
                "   t.name as targetName," +
                "   t.type as targetType, " +
                "   s.keyNumber as receiptNumber, " +
                "   s.keyYear as receiptYear, " +
                "   s.sent as receiptSentDateMillis " +
                "from " +
                "   FinancialEntry e " +
                "   inner join FinancialReference r on r.id = e.reference_entry " +
                "   inner join FinancialEntryTarget t on t.id = e.target_id " +
                "   left join FinancialReceipt s on s.entry_id = e.id " +
                "   left join UserCredentials u on u.id = e.closed_by " +
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
        Long closed = null;
        String closedBy = null;
        final CloseableFinancialEntry closeableFinancialEntry = entry.getCloseableFinancialEntry();
        if (closeableFinancialEntry != null) {
            closed = closeableFinancialEntry.getClosedDate().getMillis();
            closedBy = closeableFinancialEntry.getClosedBy().getId().toString();
        }
        jdbcTemplate.update(
                "insert into FinancialEntry (id, entry_date, entry_value, balance, reference_entry, additional_text, target_id, closed, closed_by, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                entry.getId(), DateTimeUtils.toString(entry.getEntryDate(), "yyyy-MM-dd"),
                entry.getValue().doubleValue(), entry.getBalance().getValue().doubleValue(), entry.getType().getId(),
                entry.getAdditionalText(), entry.getTarget().getId(), closed, closedBy, entry.getStatus().getValue());
    }

    @Transactional
    @Override
    public void update(FinancialEntry entry) {
        Long closed = null;
        String closedBy = null;
        final CloseableFinancialEntry closeableFinancialEntry = entry.getCloseableFinancialEntry();
        if (closeableFinancialEntry != null) {
            closed = closeableFinancialEntry.getClosedDate().getMillis();
            closedBy = closeableFinancialEntry.getClosedBy().getId().toString();
        }
        jdbcTemplate.update(
                "update FinancialEntry set entry_date=?, entry_value=?, balance=?, reference_entry=?, additional_text=?, target_id=?, closed=?, closed_by=?, status=? where id=?",
                DateTimeUtils.toString(entry.getEntryDate(), "yyyy-MM-dd"), entry.getValue().doubleValue(), entry.getBalance().getValue().doubleValue(), entry.getType().getId(),
                entry.getAdditionalText(), entry.getTarget().getId(), closed, closedBy, entry.getStatus().getValue(), entry.getId());
    }

    @Transactional
    @Override
    public void remove(String id) {
        jdbcTemplate.update("delete from FinancialEntry where id = ?", id);
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialEntry getFirstOpenedFinancialEntry() {
        return getOpenedEntry(OpenedFinancialEntryDirection.FIRST);
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialEntry getLastOpenedFinancialEntry() {
        return getOpenedEntry(OpenedFinancialEntryDirection.LAST);
    }

    private FinancialEntry getOpenedEntry(OpenedFinancialEntryDirection direction) {
        try {
            String sql =
                "select " +
                "   e.id as id," +
                "   e.entry_date," +
                "   e.entry_value," +
                "   e.balance," +
                "   e.additional_text," +
                "   e.closed, " +
                "   e.status, " +
                "   u.id as closedByUserId, " +
                "   u.username as closedByUserName, " +
                "   u.password as closedByUserPassword, " +
                "   u.user_role as closedByUserRole, " +
                "   r.id as typeId," +
                "   r.description as typeDescription," +
                "   r.category," +
                "   r.associated_with_collaborator," +
                "   t.id as targetId," +
                "   t.name as targetName," +
                "   t.type as targetType, " +
                "   s.keyNumber as receiptNumber, " +
                "   s.keyYear as receiptYear, " +
                "   s.sent as receiptSentDateMillis " +
                "from " +
                "   FinancialEntry e " +
                "   inner join FinancialReference r on r.id = e.reference_entry " +
                "   inner join FinancialEntryTarget t on t.id = e.target_id " +
                "   left join FinancialReceipt s on s.entry_id = e.id " +
                "   left join UserCredentials u on u.id = e.closed_by " +
                "where " +
                "   e.status = ? " +
                "order by e.entry_date %s limit 1";

            sql = String.format(sql, direction.sortDirection());

            return jdbcTemplate.queryForObject(sql, new Object[] { FinancialEntryStatus.OPEN.getValue() }, financialEntryRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialEntry> findOpenedEntries() {
        final String sql =
            "select " +
            "   e.id as id," +
            "   e.entry_date," +
            "   e.entry_value," +
            "   e.balance," +
            "   e.additional_text," +
            "   e.closed, " +
            "   e.status, " +
            "   u.id as closedByUserId, " +
            "   u.username as closedByUserName, " +
            "   u.password as closedByUserPassword, " +
            "   u.user_role as closedByUserRole, " +
            "   r.id as typeId," +
            "   r.description as typeDescription," +
            "   r.category," +
            "   r.associated_with_collaborator," +
            "   t.id as targetId," +
            "   t.name as targetName," +
            "   t.type as targetType, " +
            "   s.keyNumber as receiptNumber, " +
            "   s.keyYear as receiptYear, " +
            "   s.sent as receiptSentDateMillis " +
            "from " +
            "   FinancialEntry e " +
            "   inner join FinancialReference r on r.id = e.reference_entry " +
            "   inner join FinancialEntryTarget t on t.id = e.target_id " +
            "   left join FinancialReceipt s on s.entry_id = e.id " +
            "   left join UserCredentials u on u.id = e.closed_by " +
            "where " +
            "   e.status = ? " +
            "order by " +
            "   e.entry_date asc";
        return jdbcTemplate.query(sql, new Object[] { FinancialEntryStatus.OPEN.getValue() }, financialEntryRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FinancialEntry> findOpenedEntriesUntil(DateTime date) {
        final String sql =
            "select " +
            "   e.id as id," +
            "   e.entry_date," +
            "   e.entry_value," +
            "   e.balance," +
            "   e.additional_text," +
            "   e.closed, " +
            "   e.status, " +
            "   u.id as closedByUserId, " +
            "   u.username as closedByUserName, " +
            "   u.password as closedByUserPassword, " +
            "   u.user_role as closedByUserRole, " +
            "   r.id as typeId," +
            "   r.description as typeDescription," +
            "   r.category," +
            "   r.associated_with_collaborator," +
            "   t.id as targetId," +
            "   t.name as targetName," +
            "   t.type as targetType, " +
            "   s.keyNumber as receiptNumber, " +
            "   s.keyYear as receiptYear, " +
            "   s.sent as receiptSentDateMillis " +
            "from " +
            "   FinancialEntry e " +
            "   inner join FinancialReference r on r.id = e.reference_entry " +
            "   inner join FinancialEntryTarget t on t.id = e.target_id " +
            "   left join FinancialReceipt s on s.entry_id = e.id " +
            "   left join UserCredentials u on u.id = e.closed_by " +
            "where " +
            "   e.status = ? and " +
            "   e.entry_date <= datetime('" + DateTimeUtils.toString(date, "yyyy-MM-dd") + "') " +
            "order by " +
            "   e.entry_date asc";
        return jdbcTemplate.query(sql, new Object[] { FinancialEntryStatus.OPEN.getValue() }, financialEntryRowMapper);
    }
}
