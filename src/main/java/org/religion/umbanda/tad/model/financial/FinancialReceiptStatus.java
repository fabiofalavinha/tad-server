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

    public static FinancialReceiptStatus fromValue(int value) {
        for (FinancialReceiptStatus status : FinancialReceiptStatus.values()) {
            if (status.value() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format("Could not find financial receipt status by value [%d]", value));
    }
}
