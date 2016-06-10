package org.religion.umbanda.tad.model.financial;

import java.math.BigDecimal;
import java.util.Date;

public class FinancialEntry {

    private String id;
    private Date entryDate;
    private String additionalText;
    private BigDecimal value;
    private BigDecimal balance;
    private Category category;
    private FinancialReferenceEntry financialReferenceEntry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FinancialReferenceEntry getFinancialReferenceEntry() {
        return financialReferenceEntry;
    }

    public void setFinancialReferenceEntry(FinancialReferenceEntry financialReferenceEntry) {
        this.financialReferenceEntry = financialReferenceEntry;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
