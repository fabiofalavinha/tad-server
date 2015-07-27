package org.religion.umbanda.tad.model.financial;

public enum Category {

    RECEIVABLE(1),
    PAYABLE(2);

    private final int value;

    Category(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Category fromValue(int value) {
        if (value == RECEIVABLE.getValue()) {
            return RECEIVABLE;
        } else if (value == PAYABLE.getValue()) {
            return PAYABLE;
        }
        throw new IllegalArgumentException(String.format("Could not found category: %d", value));
    }
}
