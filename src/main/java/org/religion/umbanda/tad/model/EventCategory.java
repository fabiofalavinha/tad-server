package org.religion.umbanda.tad.model;

public enum EventCategory {

    GENERAL(0),
    CONSECRATION(1);

    private final int value;

    EventCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EventCategory fromValue(int value) {
        if (value == EventCategory.GENERAL.getValue()) {
            return EventCategory.GENERAL;
        } else if (value == EventCategory.CONSECRATION.getValue()) {
            return EventCategory.CONSECRATION;
        }
        throw new IllegalArgumentException(String.format("Could not found event category: %d", value));
    }
}
