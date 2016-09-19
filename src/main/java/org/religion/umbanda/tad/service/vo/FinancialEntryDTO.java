package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.religion.umbanda.tad.model.financial.Balance;

import java.math.BigDecimal;

public class FinancialEntryDTO {

    @JsonProperty
    private String id;

    @JsonProperty
    private String date;

    @JsonProperty
    private FinancialTargetVO target;

    @JsonProperty
    private FinancialReferenceVO type;

    @JsonProperty
    private String additionalText;

    @JsonProperty
    private BigDecimal value;

    @JsonProperty
    private Balance balance;

    @JsonProperty
    private Balance previewBalance;

    @JsonProperty
    private int status;

    @JsonProperty
    private CloseableFinancialEntryDTO closeableFinancialEntry;

    @JsonProperty
    private FinancialReceiptInfoDTO financialReceipt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FinancialReferenceVO getType() {
        return type;
    }

    public void setType(FinancialReferenceVO type) {
        this.type = type;
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
        return previewBalance;
    }

    public void setPreviewBalance(Balance previewBalance) {
        this.previewBalance = previewBalance;
    }

    public FinancialTargetVO getTarget() {
        return target;
    }

    public void setTarget(FinancialTargetVO target) {
        this.target = target;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setCloseableFinancialEntry(CloseableFinancialEntryDTO closeableFinancialEntry) {
        this.closeableFinancialEntry = closeableFinancialEntry;
    }

    public CloseableFinancialEntryDTO getCloseableFinancialEntry() {
        return closeableFinancialEntry;
    }

    public FinancialReceiptInfoDTO getFinancialReceipt() {
        return financialReceipt;
    }

    public void setFinancialReceipt(FinancialReceiptInfoDTO financialReceipt) {
        this.financialReceipt = financialReceipt;
    }
}
