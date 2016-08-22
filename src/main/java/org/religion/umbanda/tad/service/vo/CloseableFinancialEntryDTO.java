package org.religion.umbanda.tad.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class CloseableFinancialEntryDTO {

    @JsonProperty
    private DateTime closedDate;

    @JsonProperty
    private UserCredentialsVO userCredentialsVO;

    public DateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(DateTime closedDate) {
        this.closedDate = closedDate;
    }

    public UserCredentialsVO getUserCredentialsVO() {
        return userCredentialsVO;
    }

    public void setUserCredentialsVO(UserCredentialsVO userCredentialsVO) {
        this.userCredentialsVO = userCredentialsVO;
    }
}
