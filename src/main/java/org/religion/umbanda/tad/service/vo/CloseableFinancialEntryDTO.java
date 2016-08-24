package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloseableFinancialEntryDTO {

    @JsonProperty
    private String closedDate;

    @JsonProperty
    private UserCredentialsVO closedByUser;

    public UserCredentialsVO getClosedByUser() {
        return closedByUser;
    }

    public void setClosedByUser(UserCredentialsVO closedByUser) {
        this.closedByUser = closedByUser;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
