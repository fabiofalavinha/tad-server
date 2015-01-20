package org.religion.umbanda.tad.model;

public enum GenderType {

    MALE(0),
    FEMALE(1);

    private final int value;

    GenderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
