package org.religion.umbanda.tad.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateTimeUtils {

    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

    public static String toString(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toString(DEFAULT_DATE_TIME_FORMATTER);
    }

    private DateTimeUtils() {
    }

}
