package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloseFinancialEntryBalanceDTO {

    @JsonProperty
    private String userId;

    @JsonProperty
    private String closingDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }
}
