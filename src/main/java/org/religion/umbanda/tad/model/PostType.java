package org.religion.umbanda.tad.model;

public enum PostType {
    
    GENERAL(0),
    CLASS(1);

    public static PostType fromValue(int value) {
        if (GENERAL.getValue() == value) {
            return GENERAL;
        } else if (CLASS.getValue() == value) {
            return  CLASS;
        }
        throw new IllegalArgumentException(String.format("Invalid post type: %d", value));
    }

    private final int value;

    PostType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}