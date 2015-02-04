package org.religion.umbanda.tad.model;

public enum VisibilityType {
    
    INTERNAL(0),
    PUBLIC(1);

    private final int value;

    VisibilityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VisibilityType fromValue(int value) {
        if (value == VisibilityType.INTERNAL.getValue()) {
            return VisibilityType.INTERNAL;
        } else if (value == VisibilityType.PUBLIC.getValue()) {
            return VisibilityType.PUBLIC;
        }
        throw new IllegalArgumentException(String.format("Could not found visibility type: %d", value));
    }

}