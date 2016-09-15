package org.religion.umbanda.tad.model.financial;

public enum FinancialReceiptStatus {

    CREATED(0),
    SENT(1),
    ERROR(2);

    private final int value;

    FinancialReceiptStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
