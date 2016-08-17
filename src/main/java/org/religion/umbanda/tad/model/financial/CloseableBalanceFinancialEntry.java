package org.religion.umbanda.tad.model.financial;

import org.apache.catalina.User;
import org.joda.time.DateTime;

public class CloseableBalanceFinancialEntry {

    private DateTime closedDate;
    private User closedBy;
    private Balance balance;

    public DateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(DateTime closedDate) {
        this.closedDate = closedDate;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(User closedBy) {
        this.closedBy = closedBy;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

}
