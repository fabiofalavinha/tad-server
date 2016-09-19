package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;

public class FinancialReceiptInfo {

    private String number;
    private DateTime sent;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DateTime getSent() {
        return sent;
    }

    public void setSent(DateTime sent) {
        this.sent = sent;
    }
}
