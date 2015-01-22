package org.religion.umbanda.tad.model;

public enum UserRole {

    ADMINISTRATOR_ROLE(0),
    FINANCIAL_ROLE(1),
    COLLABORATOR(2);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
