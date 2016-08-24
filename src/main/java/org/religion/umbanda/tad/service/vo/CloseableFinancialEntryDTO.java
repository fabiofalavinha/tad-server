package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloseableFinancialEntryDTO {

    @JsonProperty
    private String closedDate;

    @JsonProperty
    private UserCredentialsVO userCredentialsVO;

    public UserCredentialsVO getUserCredentialsVO() {
        return userCredentialsVO;
    }

    public void setUserCredentialsVO(UserCredentialsVO userCredentialsVO) {
        this.userCredentialsVO = userCredentialsVO;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
