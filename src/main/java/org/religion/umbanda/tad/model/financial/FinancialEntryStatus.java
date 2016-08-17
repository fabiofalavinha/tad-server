package org.religion.umbanda.tad.model.financial;

public enum FinancialEntryStatus {

    OPEN(0),
    CLOSED(1);

    private final int value;

    FinancialEntryStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FinancialEntryStatus fromValue(int value) {
        if (value == FinancialEntryStatus.OPEN.getValue()) {
            return FinancialEntryStatus.OPEN;
        } else if (value == FinancialEntryStatus.CLOSED.getValue()) {
            return FinancialEntryStatus.CLOSED;
        } else {
            throw new IllegalArgumentException(String.format("Financial entry status not found [%d]", value));
        }
    }
}
