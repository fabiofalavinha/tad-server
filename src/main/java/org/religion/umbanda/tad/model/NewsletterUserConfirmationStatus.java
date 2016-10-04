package org.religion.umbanda.tad.model;

import java.util.Arrays;
import java.util.Optional;

public enum NewsletterUserConfirmationStatus {

    PENDING(0),
    CONFIRMED(1);

    private final int value;

    NewsletterUserConfirmationStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Optional<NewsletterUserConfirmationStatus> fromValue(int value) {
        return Arrays.stream(values()).filter(s -> s.value() == value).findAny();
    }
}
