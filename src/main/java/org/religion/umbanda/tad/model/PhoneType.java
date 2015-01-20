package org.religion.umbanda.tad.model;

public enum PhoneType {

    HOME(0),
    WORK(1),
    MOBILE(2);

    private final int value;

    PhoneType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
