package org.religion.umbanda.tad.service.vo;

import java.math.BigDecimal;

public class FinancialEntryDTO {

    private String id;
    private int category;
    private String entryDate;
    private BigDecimal value;
    private String referenceEntry;
    private String additionalText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getReferenceEntry() {
        return referenceEntry;
    }

    public void setReferenceEntry(String referenceEntry) {
        this.referenceEntry = referenceEntry;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }
}
