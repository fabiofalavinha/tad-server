package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.UserCredentials;

import java.util.UUID;

public class CloseableBalanceFinancialEntry {

    private UUID id;
    private DateTime closedDate;
    private UserCredentials closedBy;
    private Balance balance;

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

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
