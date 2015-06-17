package org.religion.umbanda.tad.util;

import java.util.UUID;

public final class IdUtils {

    private IdUtils() {
    }

    public static UUID fromString(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Id inválido", ex);
        }
    }

}
