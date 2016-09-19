package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.FinancialEntry;
import org.religion.umbanda.tad.model.financial.FinancialReceipt;
import org.religion.umbanda.tad.model.financial.FinancialReceiptKey;
import org.religion.umbanda.tad.model.financial.FinancialReceiptStatus;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.FinancialEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
class FinancialReceiptRepositoryImpl implements FinancialReceiptRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FinancialEntryRepository financialEntryRepository;
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    FinancialReceiptRepositoryImpl(JdbcTemplate jdbcTemplate, FinancialEntryRepository financialEntryRepository, CollaboratorRepository collaboratorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.financialEntryRepository = financialEntryRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialReceiptKey generateKey() {
        try {
            return jdbcTemplate.queryForObject("select MAX(keyNumber) as keyNumber from FinancialReceiptId", (resultSet, i) -> new FinancialReceiptKey(resultSet.getInt("keyNumber") + 1, DateTime.now().getYear()));
        } catch (Exception ex) {
            return FinancialReceiptKey.geneterateDefault();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public FinancialReceipt findByEntryId(String entryId) {
        Optional<FinancialReceipt> financialReceiptMaybe;
        try {
            financialReceiptMaybe = jdbcTemplate.queryForObject("select * from FinancialReceipt where entry_id = ?", ((resultSet, i) -> {
                FinancialReceipt receipt = new FinancialReceipt();
                receipt.setKey(new FinancialReceiptKey(resultSet.getInt("keyNumber"), resultSet.getInt("keyYear")));
                receipt.setCreated(new DateTime(resultSet.getLong("created")));
                receipt.setSent(new DateTime(resultSet.getLong("sent")));
                receipt.setStatus(FinancialReceiptStatus.fromValue(resultSet.getInt("status")));
                return Optional.of(receipt);
            }), entryId);
        } catch (EmptyResultDataAccessException ex) {
            financialReceiptMaybe = Optional.empty();
        }
        if (financialReceiptMaybe.isPresent()) {
            FinancialReceipt financialReceipt = financialReceiptMaybe.get();
            FinancialEntry financialEntry = financialEntryRepository.findById(entryId);
            financialReceipt.setFinancialEntry(financialEntry);
            financialReceipt.setCollaborator(collaboratorRepository.findById(financialEntry.getTarget().getIdAsUUID()));
            return financialReceipt;
        }
        return null;
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
