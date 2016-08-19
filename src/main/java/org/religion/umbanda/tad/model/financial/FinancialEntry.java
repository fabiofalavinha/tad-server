package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class FinancialEntry {

    private String id;
    private DateTime entryDate;
    private String additionalText;
    private BigDecimal value;
    private Balance balance;
    private FinancialReference type;
    private FinancialTarget target;
    private FinancialEntryStatus status;
    private CloseableFinancialEntry closeableFinancialEntry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(DateTime entryDate) {
        this.entryDate = entryDate;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Balance getPreviewBalance() {
        return getBalance().calculate(getValue(), type.getCategory());
    }

    public FinancialTarget getTarget() {
        return target;
    }

    public void setTarget(FinancialTarget target) {
        this.target = target;
    }

    public FinancialReference getType() {
        return type;
    }

    public void setType(FinancialReference type) {
        this.type = type;
    }

    public CloseableFinancialEntry getCloseableFinancialEntry() {
        return closeableFinancialEntry;
    }

    public void setCloseableFinancialEntry(CloseableFinancialEntry closeableFinancialEntry) {
        this.closeableFinancialEntry = closeableFinancialEntry;
    }

    public FinancialEntryStatus getStatus() {
        return status;
    }

    public void setStatus(FinancialEntryStatus status) {
        this.status = status;
    }
}
