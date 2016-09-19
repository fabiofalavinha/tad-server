package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;

public class FinancialReceiptKey {

    public static FinancialReceiptKey geneterateDefault() {
        return new FinancialReceiptKey(1, DateTime.now().getYear());
    }

    public static String from(int receiptNumber, int receiptYear) {
        return receiptNumber + "/" + receiptYear;
    }

    private final int number;
    private final int year;

    public FinancialReceiptKey(int number, int year) {
        this.number = number;
        this.year = year;
    }

    public int getNumber() {
        return number;
    }

    public int getYear() {
        return year;
    }

    public String value() {
        return String.valueOf(number).concat("/").concat(String.valueOf(year));
    }
}
