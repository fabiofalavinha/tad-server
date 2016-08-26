package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloseableFinancialEntryDTO {

    @JsonProperty
    private String closedDate;

    @JsonProperty
    private UserCredentialsVO closedBy;

    public UserCredentialsVO getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(UserCredentialsVO closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
