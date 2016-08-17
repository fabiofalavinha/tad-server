package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.UserCredentials;

public class CloseableFinancialEntry {

    private DateTime closedDate;
    private UserCredentials closedBy;

    public DateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(DateTime closedDate) {
        this.closedDate = closedDate;
    }

    public UserCredentials getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(UserCredentials closedBy) {
        this.closedBy = closedBy;
    }
}
