package org.religion.umbanda.tad.model.financial;

import java.math.BigDecimal;
import java.util.Date;

public class FinancialEntry {

    private String id;
    private Date entryDate;
    private String additionalText;
    private BigDecimal value;
    private BigDecimal balance;
    private BigDecimal previewBalance;
    private FinancialReference type;
    private FinancialTarget target;
    private Date created;
    private String createdBy;
    private Date modified;
    private String modifiedBy;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPreviewBalance() {
        return previewBalance;
    }

    public void setPreviewBalance(BigDecimal previewBalance) {
        this.previewBalance = previewBalance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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
}
