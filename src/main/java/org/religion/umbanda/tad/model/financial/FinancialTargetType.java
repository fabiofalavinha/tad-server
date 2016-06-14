package org.religion.umbanda.tad.model.financial;

public enum FinancialTargetType {

    COLLABORATOR(0),
    NON_COLLABORATOR(1);

    private final int value;

    FinancialTargetType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static FinancialTargetType fromValue(int value) {
        if (COLLABORATOR.value() == value) return COLLABORATOR;
        if (NON_COLLABORATOR.value() == value) return NON_COLLABORATOR;
        throw new IllegalArgumentException(String.format("Could not find financial target by value [%d]", value));
    }
}
